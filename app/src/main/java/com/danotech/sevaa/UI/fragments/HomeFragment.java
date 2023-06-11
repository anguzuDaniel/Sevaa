package com.danotech.sevaa.UI.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.danotech.sevaa.Model.CreditCard;
import com.danotech.sevaa.Model.Expense;
import com.danotech.sevaa.Model.ProfileHandler;
import com.danotech.sevaa.Model.Savings;
import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.danotech.sevaa.helpers.DateConversion;
import com.danotech.sevaa.helpers.ExpenseType;
import com.danotech.sevaa.helpers.MyRecyclerViewAdapter;
import com.danotech.sevaa.helpers.ThemeManager;
import com.danotech.sevaa.helpers.ThemeUtils;
import com.google.android.gms.location.LocationListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {
    User user;
    Savings savings;
    CreditCard creditCard;
    TextView balance, income, expense, userName;
    private BottomSheetDialog bottomSheetDialog;
    ExpenseType expenseType;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private List<Expense> dataList;
    Expense exp;
    private StorageReference storageReference;
    ImageView imageView;
    View expenseCard;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    LinearLayout background;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Apply the stored theme mode
        int themeMode = ThemeManager.getThemeMode(getContext());
        AppCompatDelegate.setDefaultNightMode(themeMode);
        background = view.findViewById(R.id.background);

        // Obtain the SharedPreferences instance
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Create the preference change listener
        preferenceChangeListener = (sharedPrefs, key) -> {
            if (key.equals("theme")) {
                boolean isDarkModeEnabled = sharedPreferences.getBoolean(key, false);
                updateBackgroundColors(background, isDarkModeEnabled);
            }
        };

        // Register the preference change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        user = new User();
        creditCard = new CreditCard();
        balance = view.findViewById(R.id.balance);
        income = view.findViewById(R.id.income);
        expense = view.findViewById(R.id.expense);
        userName = view.findViewById(R.id.user_name);
        storageReference = FirebaseStorage.getInstance().getReference();
        imageView = view.findViewById(R.id.profile_image);


        Context context = getContext();
        ExtendedFloatingActionButton fab = view.findViewById(R.id.extended_fab_expense);

        fab.setOnClickListener(v -> {
            showBottomSheetBudget(context);
        });

        displayUserProfileInfo();

        if (user != null && user.hasProfile(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())) {
            userName.setText(user.getUsername());
        } else {
            userName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        }


        // Add swipe-to-delete functionality
        ItemTouchHelper.SimpleCallback swipeToDeleteCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // Not needed for swipe-to-delete functionality
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                dataList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        displayExpenseCards(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void updateBackgroundColors(View view, boolean isDarkModeEnabled) {
        ThemeUtils.updateBackgroundColor(view, isDarkModeEnabled);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity().getApplicationContext();
        displayProfileInfo(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayExpenseCards(getView());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bottomSheetDialog = null;
    }

    public void displayExpenseCards(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("expense")
                .whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout budgetContainer = view.findViewById(R.id.expense_container);

                            String name = document.getString("name") != null ? document.getString("name") : "";
                            String price = document.getString("price") != null ? document.getString("price") : "";
                            String date = document.getString("date") != null ? document.getString("date") : "";
                            String expenseType = document.getString("expense_type") != null ? document.getString("expense_type") : "payment";

                            expenseCard = getLayoutInflater().inflate(R.layout.expense_card, null);

                            TextView nameTextView = expenseCard.findViewById(R.id.expense_name);
                            TextView priceTextView = expenseCard.findViewById(R.id.expense_price);
                            TextView expenseTypeTextView = expenseCard.findViewById(R.id.expense_type);
                            TextView dateTextView = expenseCard.findViewById(R.id.expense_date);

                            nameTextView.setText(name);

                            priceTextView.setText(Currency.getInstance(Locale.getDefault()).toString() + "" + price);

                            expenseTypeTextView.setText(expenseType.toLowerCase());

                            dateTextView.setText(DateConversion.convert(date));

                            // Add the expenseCard view to the budgetContainer
                            budgetContainer.addView(expenseCard);

                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("balance", balance.getText().toString());
        outState.putString("income", income.getText().toString());
        outState.putString("expense", expense.getText().toString());
        outState.putString("userName", userName.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            balance.setText(savedInstanceState.getString("balance"));
            income.setText(savedInstanceState.getString("income"));
            expense.setText(savedInstanceState.getString("expense"));
            userName.setText(savedInstanceState.getString("userName"));
        }
    }

    private void showBottomSheetBudget(Context context) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(getLayoutInflater()
                    .inflate(R.layout.fragment_bottom_sheet_expense, null));
            bottomSheetDialog.setCanceledOnTouchOutside(true); // Allow the user to dismiss the bottom sheet by tapping outside it

            EditText expenseTitleText = bottomSheetDialog.findViewById(R.id.expense_name);
            EditText expenseNameText = bottomSheetDialog.findViewById(R.id.expense_price);
            String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

            // Declare and initialize the expenseType variable
            expenseType = ExpenseType.UNKNOWN;


            Resources res = getResources();
            int[] expenseTypeIds = res.getIntArray(R.array.expense_type_ids);
            String[] expenseTypeNames = res.getStringArray(R.array.expense_type);

            AutoCompleteTextView autoCompleteExpenseType = bottomSheetDialog.findViewById(R.id.expense_type);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, expenseTypeNames);
            autoCompleteExpenseType.setAdapter(adapter);

            autoCompleteExpenseType.setOnItemClickListener((parent, view, position, id) -> {
                // Get the selected item's ID
                // Get the selected expense type ID based on the position
                Log.d(TAG, "POSITION IS :" + position);
                int selectedExpenseTypeId = expenseTypeIds[(int) id];

                // Use the selected expense type ID to get the ExpenseType object
                expenseType = ExpenseType.getExpenseTypeById(selectedExpenseTypeId);

            });

            // Submit button logic
            Button submitButton = bottomSheetDialog.findViewById(R.id.button_submit);
            assert submitButton != null;
            submitButton.setOnClickListener(v -> {
                // Get credit card information from form
                assert expenseTitleText != null;
                String expenseName = expenseTitleText.getText().toString();
                String expensePrice = expenseNameText.getText().toString();

                // creates and saves an expense
                exp = new Expense(expenseName, expenseType, expensePrice, timeStamp);
                exp.Save();

                Toast.makeText(getContext(), "Budget added", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }


    public void displayUserProfileInfo() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userEmail = currentUser.getEmail();

        String currency = Currency.getInstance(Locale.getDefault()).getCurrencyCode();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("savings")
                .whereEqualTo("userID", userEmail)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                    return FirebaseFirestore.getInstance().collection("savings")
                            .document(userEmail).get();
                })
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        savings = documentSnapshot.toObject(Savings.class);
                        creditCard = documentSnapshot.toObject(CreditCard.class);

                        balance.setText(currency + savings.getBalance());
                        income.setText(currency + savings.getIncome());
                        expense.setText(currency + savings.getExpenses());

                        System.out.println(creditCard);
                    }
                })
                .addOnFailureListener(e -> {
                    balance.setText(currency + 0.0);
                    income.setText(currency + 0.0);
                    expense.setText(currency + 0.0);
                    Log.d(TAG, "onFailure: " + e);
                });

    }

    public void displayProfileInfo(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a reference to the user document
        DocumentReference userRef = db
                .collection("users")
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail());

        // Call the get() method on the reference to retrieve the document
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userName.setText(document.getString("name"));
                    // User document exists in the users collection

                    if (imageView != null) {
                        ProfileHandler profileHandler = new ProfileHandler(storageReference, imageView);
                        profileHandler.loadProfileImage(context, document.getString("profileImageUrl"));
                    }
                }
            } else {
                Log.d(TAG, "Failed to retrieve user document from the users collection", task.getException());
            }
        });
    }
}