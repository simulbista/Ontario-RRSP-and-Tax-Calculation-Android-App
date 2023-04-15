package com.simul.ontariotaxrrspcalculation2022;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    double totalTaxValue;
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
        rrspLimit2024 = Math.round(rrspLimit2023 - rrspContribution + rrspMaxLimit2024 * 100.0) / 100.0;
        RRSPLimit.setText("$" + String.valueOf(rrspLimit2024));

        //calculate taxable income based on rrsp contribution
        annualIncomeValue = Double.parseDouble(annualIncome.getText().toString());

        //RRSP rule
        // the max value upto which your taxable amount is reduced is 18% of your annual income, rrsp beyond that wont reduce your taxable income)
        //if you submit more than 18% as rrsp (provided that it is less than contribution room), still the taxable income remains the same
        if(rrspContribution>0.18*annualIncomeValue){
            taxableIncomeValue = annualIncomeValue - 0.18*annualIncomeValue;
        }else{
            taxableIncomeValue = annualIncomeValue - rrspContribution;
        }

        //display toast msg if rrsp amount is greater than annual income
        if(rrspContribution>annualIncomeValue){
            Toast.makeText(getApplicationContext(), "RRSP contribution cannot be more than the annual income!", Toast.LENGTH_SHORT).show();
            return;
        }
        taxableIncome.setText("$" + String.valueOf(Math.round(taxableIncomeValue * 100.0) / 100.0));

        //calling method to calculate federal tax and storing the result into a variable
        federalTaxValue = calculateFederalTax(taxableIncomeValue);
        federalTax.setText("$" + String.valueOf(federalTaxValue));

        //calling method to calculate federal tax and storing the result into a variable
        provincialTaxValue = calculateProvincialTax(taxableIncomeValue);
        provincialTax.setText("$" + String.valueOf(provincialTaxValue));

        //finally summing both federal and provincial tax to return the total tax to be paid
        totalTaxValue = federalTaxValue + provincialTaxValue;
        //rounding to 2 decimal places
        totalTax.setText("$" + String.valueOf(Math.round(totalTaxValue * 100.0) / 100.0));
    }

    //method to calculate federal tax
    private double calculateFederalTax(double taxableIncome){
//        tax bracket for 2023
//        15% on the portion of taxable income that is $53,359 or less, plus
//        20.5% on the portion of taxable income over $53,359 up to $106,717, plus
//        26% on the portion of taxable income over $106,717 up to $165,430, plus
//        29% on the portion of taxable income over $165,430 up to $235,675, plus
//        33% on the portion of taxable income over $235,675

        double federalTax = 0;

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

        //returning federal tax rounded to 2 decimal places
        return Math.round(federalTax * 100.0) / 100.0;
    }

    //method to calculate provincial (Ontario) tax
    private double calculateProvincialTax(double taxableIncome) {
//        tax bracket for 2023
//        5.05% on the portion of your taxable income that is $49,231 or less, plus
//        9.15% on the portion of your taxable income over $49,231 up to $98,463, plus
//        11.16% on the portion of your taxable income over $98,463 up to $150,000, plus
//        12.16% on the portion of your taxable income over $150,000 up to $220,000, plus
//        13.16% on the portion of your taxable income over $220,000

        double provincialTax = 0;

        if (taxableIncome > 220000) {
            provincialTax += 0.1316 * (taxableIncome - 220000);
            taxableIncome = 220000;
        }

        if (taxableIncome > 150000) {
            provincialTax += 0.1216 * (taxableIncome - 150000);
            taxableIncome = 150000;
        }

        if (taxableIncome > 98463) {
            provincialTax += 0.1116 * (taxableIncome - 98463);
            taxableIncome = 98463;
        }

        if (taxableIncome > 49231) {
            provincialTax += 0.0915 * (taxableIncome - 49231);
            taxableIncome = 49231;
        }

        provincialTax += 0.0505 * taxableIncome;

        return Math.round(provincialTax * 100.0) / 100.0;
    }

}
