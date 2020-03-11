package com.tommy.basic;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText firstEditText;
    private EditText secondEditText;
    private RadioGroup operationChoice;
    private CheckBox floatNumbers;
    private CheckBox signedNumbers;
    private TextView resultOfCalculations;
    private Button calculateButton;

    private boolean isFloatNumber = true;
    private boolean isSignedNumber = true;
    private Operation operation;
    private Double numberOne;
    private Double numberTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO read from previous state
        initializeAllViews();
        setRadioButtonsListeners();
        setCheckBoxesListeners();
        setTextEditorsListener();

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operation == null) {
                    showExceptionToast(getString(R.string.operation_not_found));
                } else {
                    if (isNumbersCorrect()) {
                        resultOfCalculations.setText(String.valueOf(calculate(numberOne, numberTwo, operation)));
                        //TODO set to state
                    } else {
                        showExceptionToast(getString(R.string.value_cannot_be_empty));
                    }
                }
            }
        });
    }

    private void initializeAllViews() {
        firstEditText = findViewById(R.id.field1);
        secondEditText = findViewById(R.id.field2);
        operationChoice = findViewById(R.id.radioOperationGroup);
        floatNumbers = findViewById(R.id.floatValues);
        signedNumbers = findViewById(R.id.signedValues);
        resultOfCalculations = findViewById(R.id.resultField);
        calculateButton = findViewById(R.id.calculateButton);
    }

    private void setRadioButtonsListeners() {
        operationChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setOperationChoiceState();
            }
        });
    }

    private void setCheckBoxesListeners() {
        floatNumbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFloatNumber = isChecked;
                setInputTypeToTextViews();
            }
        });
        signedNumbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSignedNumber = isChecked;
                setInputTypeToTextViews();
            }
        });
    }

    private void setTextEditorsListener() {
        firstEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    numberOne = Double.valueOf(String.valueOf(firstEditText.getText()));
                } catch (NumberFormatException e) {
                    numberOne = null;
                    showExceptionToast(e.getLocalizedMessage());
                }
            }
        });
        secondEditText.setOnFocusChangeListener(getListenerForEditTextForms());
    }

    private View.OnFocusChangeListener getListenerForEditTextForms() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    switch (v.getId()) {
                        case R.id.field1:
                            numberOne = Double.valueOf(String.valueOf(firstEditText.getText()));
                            break;
                        case R.id.field2:
                            numberTwo = Double.valueOf(String.valueOf(firstEditText.getText()));
                            break;
                    }
                } catch (NumberFormatException e) {
                    showExceptionToast(e.getLocalizedMessage());
                }
            }
        };
    }

    private Double calculate(double a, double b, Operation operation) {
        try {
            switch (operation) {
                case PLUS:
                    return a + b;
                case MINUS:
                    return a - b;
                case DIVIDE:
                    return a / b;
                case MULTIPLY:
                    return a * b;
                default:
                    return null;
            }
        } catch (ArithmeticException e) {
            showExceptionToast(e.getLocalizedMessage());
        }
        return null;
    }

    private void showExceptionToast(String localizedMessage) {
        Toast.makeText(getApplicationContext(), localizedMessage, Toast.LENGTH_LONG).show();
    }

    private void setInputTypeToTextViews() {
        int inputType = getInputType(isFloatNumber, isSignedNumber);
        firstEditText.setInputType(inputType);
        secondEditText.setInputType(inputType);
        //TODO set to state
    }

    private int getInputType(boolean isFloatNumber, boolean isSignedNumber) {
        final int numericType = InputType.TYPE_CLASS_NUMBER;
        if (isFloatNumber && isSignedNumber) {
            return numericType | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED;
        } else if (!isFloatNumber && !isSignedNumber) {
            return numericType | InputType.TYPE_NUMBER_VARIATION_NORMAL;
        } else {
            int additionalNumericType = isSignedNumber ? InputType.TYPE_NUMBER_FLAG_SIGNED : InputType.TYPE_NUMBER_FLAG_DECIMAL;
            return numericType | additionalNumericType;
        }
    }

    private void setOperationChoiceState() {
        switch (operationChoice.getCheckedRadioButtonId()) {
            case R.id.operation1:
                operation = Operation.PLUS;
                break;
            case R.id.operation2:
                operation = Operation.MINUS;
                break;
            case R.id.operation3:
                operation = Operation.DIVIDE;
                break;
            case R.id.operation4:
                operation = Operation.MULTIPLY;
                break;
            default:
                operation = null;
        }
        //TODO set to state
    }

    private boolean isNumbersCorrect() {
        return (numberOne != null && numberTwo != null);
    }
}
