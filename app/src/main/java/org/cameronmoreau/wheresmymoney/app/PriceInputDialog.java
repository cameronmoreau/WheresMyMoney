package org.cameronmoreau.wheresmymoney.app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.cameronmoreau.wheresmymoney.R;

import java.util.ArrayList;

/**
 * Created by Cameron on 11/20/2014.
 */
public class PriceInputDialog extends DialogFragment implements View.OnClickListener {

    private Button button;
    private ViewHolder holder;

    private ArrayList<Character> output;

    private static class ViewHolder {
        TextView price;
        Button numberOne, numberTwo, numberThree, numberFour, numberFive,
                numberSix, numberSeven, numberEight, numberNine, numberZero,
                numberDoubleZero, buttonDone, buttonClear;
        ImageButton buttonBackspace;
    }

    public PriceInputDialog() {
    }

    public void setLinkedButton(Button button) {
        this.button = button;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_price_input, null);

        Typeface lightFont = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Roboto-Light.ttf");

        output = new ArrayList<Character>();
        resetOutput();

        holder = new ViewHolder();
        holder.price = (TextView) rootView.findViewById(R.id.tv_price_input);
        holder.buttonDone = (Button) rootView.findViewById(R.id.button_price_input_done);
        holder.buttonClear = (Button) rootView.findViewById(R.id.button_price_input_clear);
        holder.buttonBackspace = (ImageButton) rootView.findViewById(R.id.button_price_input_delete);

        holder.numberOne = (Button) rootView.findViewById(R.id.button_price_input_one);
        holder.numberTwo = (Button) rootView.findViewById(R.id.button_price_input_two);
        holder.numberThree = (Button) rootView.findViewById(R.id.button_price_input_three);
        holder.numberFour = (Button) rootView.findViewById(R.id.button_price_input_four);
        holder.numberFive = (Button) rootView.findViewById(R.id.button_price_input_five);
        holder.numberSix = (Button) rootView.findViewById(R.id.button_price_input_six);
        holder.numberSeven = (Button) rootView.findViewById(R.id.button_price_input_seven);
        holder.numberEight = (Button) rootView.findViewById(R.id.button_price_input_eight);
        holder.numberNine = (Button) rootView.findViewById(R.id.button_price_input_nine);
        holder.numberZero = (Button) rootView.findViewById(R.id.button_price_input_zero);
        holder.numberDoubleZero = (Button) rootView.findViewById(R.id.button_price_input_doublezero);

        holder.buttonDone.setOnClickListener(this);
        holder.buttonClear.setOnClickListener(this);
        holder.buttonBackspace.setOnClickListener(this);
        holder.numberOne.setOnClickListener(this);
        holder.numberTwo.setOnClickListener(this);
        holder.numberThree.setOnClickListener(this);
        holder.numberFour.setOnClickListener(this);
        holder.numberFive.setOnClickListener(this);
        holder.numberSix.setOnClickListener(this);
        holder.numberSeven.setOnClickListener(this);
        holder.numberEight.setOnClickListener(this);
        holder.numberNine.setOnClickListener(this);
        holder.numberZero.setOnClickListener(this);
        holder.numberDoubleZero.setOnClickListener(this);

        //holder.buttonDone.setTypeface(lightFont);
        holder.buttonClear.setTypeface(lightFont);
        holder.numberOne.setTypeface(lightFont);
        holder.numberTwo.setTypeface(lightFont);
        holder.numberThree.setTypeface(lightFont);
        holder.numberFour.setTypeface(lightFont);
        holder.numberFive.setTypeface(lightFont);
        holder.numberSix.setTypeface(lightFont);
        holder.numberSeven.setTypeface(lightFont);
        holder.numberEight.setTypeface(lightFont);
        holder.numberNine.setTypeface(lightFont);
        holder.numberZero.setTypeface(lightFont);
        holder.numberDoubleZero.setTypeface(lightFont);

        return rootView;
    }

    private void numPress(char number) {
        if(output.size() > 8)
            return;

        if (number == 'z') {
            if(!output.toString().equals("[0, 0, 0]")) {
                this.output.add('0');
                this.output.add('0');
                if (this.output.get(0) == '0') {
                    this.output.remove(0);
                }
            } else {
                return;
            }
        } else {
            this.output.add(number);
        }

        if (this.output.get(0) == '0') {
            this.output.remove(0);
        }

        this.holder.price.setText(outputToPrice());
    }

    private void deleteNumPress() {
        if (this.output.size() < 4)
            this.output = arrayListPrepend(this.output, '0');

        this.output.remove(this.output.size() - 1);

        this.holder.price.setText(outputToPrice());
    }

    private ArrayList<Character> arrayListPrepend(ArrayList<Character> items, Character value) {
        ArrayList<Character> new_list = new ArrayList<Character>();

        new_list.add(value);
        new_list.addAll(items);

        return new_list;
    }

    private String outputToPrice() {
        String s = "$";
        for (int i = 0; i < this.output.size(); i++) {
            if (i == this.output.size() - 2) {
                s += ".";
            }

            s += this.output.get(i);
        }
        return s;
    }

    private void resetOutput() {
        this.output.clear();
        this.output.add('0');
        this.output.add('0');
        this.output.add('0');
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_price_input_done:
                if(!output.toString().equals("[0, 0, 0]"))
                    button.setText(outputToPrice());
                getDialog().dismiss();
                break;
            case R.id.button_price_input_clear:
                resetOutput();
                this.holder.price.setText(outputToPrice());
                break;
            case R.id.button_price_input_delete:
                deleteNumPress();
                break;

            case R.id.button_price_input_one:
                numPress('1');
                break;
            case R.id.button_price_input_two:
                numPress('2');
                break;
            case R.id.button_price_input_three:
                numPress('3');
                break;
            case R.id.button_price_input_four:
                numPress('4');
                break;
            case R.id.button_price_input_five:
                numPress('5');
                break;
            case R.id.button_price_input_six:
                numPress('6');
                break;
            case R.id.button_price_input_seven:
                numPress('7');
                break;
            case R.id.button_price_input_eight:
                numPress('8');
                break;
            case R.id.button_price_input_nine:
                numPress('9');
                break;
            case R.id.button_price_input_zero:
                numPress('0');
                break;
            case R.id.button_price_input_doublezero:
                numPress('z');
                break;

        }
    }
}
