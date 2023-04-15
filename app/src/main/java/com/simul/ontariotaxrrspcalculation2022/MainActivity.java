package com.simul.ontariotaxrrspcalculation2022;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    float rrspContribution;
    Button calculateButton;
    TextView annualIncome;
    double annualIncomeValue;
    TextView contributionValue;
    Slider contributionSlider;
    TextView RRSPLimit;
    TextView taxableIncome;
    double taxableIncomeValue;
    double federalTaxValue;
    double provincialTaxValue;
    TextView federalTax;
    TextView provincialTax;
    TextView totalTax;
    final double rrspLimit2023 = 29210;
    double rrspMaxLimit2024 = 30780;
    double rrspLimit2024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        annualIncome = findViewById(R.id.income_value);
        contributionValue = findViewById(R.id.rrsp_contribution_value);
        contributionSlider = findViewById(R.id.rrsp_contribution_slider);
        RRSPLimit = findViewById(R.id.rrsp_limit_value);
        taxableIncome = findViewById(R.id.taxable_income_value);
        federalTax = findViewById(R.id.federal_tax_value);
        provincialTax = findViewById(R.id.provincial_tax_value);
        totalTax = findViewById(R.id.total_tax_value);

        //method to take value from slider and display it back
        contributionSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                //round upto 2 decimal places
                rrspContribution = (float) Math.round(value * 100) / 100;
                contributionValue.setText("$" + String.valueOf(rrspContribution));
            }
        });

        calculateButton = findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call method to calculate rrsp limit for 2024 and the total tax (federal plus provincial)
                calculate();
            }
        });
    }

    private void calculate() {
        //calculate rrsp limit for 2024
        rrspLimit2024 = rrspLimit2023 - rrspContribution + rrspMaxLimit2024;
        RRSPLimit.setText(String.valueOf(rrspLimit2024));

        //calculate taxable income based on rrsp contribution
        annualIncomeValue = Double.parseDouble(annualIncome.getText().toString());
        taxableIncomeValue = annualIncomeValue - rrspContribution;
        taxableIncome.setText(String.valueOf(taxableIncomeValue));

        //calling method to calculate federal tax and storing the result into a variable
        federalTaxValue = calculateFederalTax(taxableIncomeValue);
        federalTax.setText(String.valueOf(federalTaxValue));

    }

    private double calculateFederalTax(double taxableIncomeValue){
        double federalTax = 0;
        double taxableIncome = taxableIncomeValue;

        if (taxableIncome > 235675) {
            federalTax += 0.33 * (taxableIncome - 235675);
            taxableIncome = 235675;
        }

        if (taxableIncome > 165430) {
            federalTax += 0.29 * (taxableIncome - 165430);
            taxableIncome = 165430;
        }

        if (taxableIncome > 106717) {
            federalTax += 0.26 * (taxableIncome - 106717);
            taxableIncome = 106717;
        }

        if (taxableIncome > 53359) {
            federalTax += 0.205 * (taxableIncome - 53359);
            taxableIncome = 53359;
        }

        federalTax += 0.15 * taxableIncome;

        return federalTax;
    }
}
