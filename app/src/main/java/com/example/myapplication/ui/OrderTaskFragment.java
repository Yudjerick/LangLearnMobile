package com.example.myapplication.ui;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.OrderTask;
import com.example.myapplication.viewmodels.OrderTaskViewModel;
import com.example.myapplication.databinding.FragmentOrderTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderTaskFragment extends Fragment {
    private FragmentOrderTaskBinding binding;
    private OrderTask task;
    private Flow bankFlow;
    private Flow answerFlow;

    private OrderTaskViewModel model;
    public OrderTaskFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("dark", false)){
            binding.getRoot().setBackgroundColor(getResources().getColor(R.color.dark_background));
        }

        model = new ViewModelProvider(getActivity()).get(OrderTaskViewModel.class);
        model.getAnswer().observe(getViewLifecycleOwner(), a ->{
            setAnswerUI(model.getAnswer().getValue());
        });
        model.getBank().observe(getViewLifecycleOwner(), a->{
            setBankUI(model.getBank().getValue());
        });

        String s = "Он обещал закончить проект через неделю";
        String[] words = {"He","promised","to", "finish", "the", "project", "in", "one", "week"};
        String[] additional = {"day", "she"};
        OrderTask task1 = new OrderTask(s,words, additional);
        if(model.getTask() == null){
            setTask(task1);
        }
        else{
            setTask(model.getTask());
        }

        binding.checkButton.setOnClickListener(view1 -> {
            ArrayList<String> givenAnswer = new ArrayList<>();
            for(int index = 0; index < ((ViewGroup) binding.answerConstraintLayout).getChildCount(); index++) {
                View nextChild = ((ViewGroup) binding.answerConstraintLayout).getChildAt(index);
                try {
                    givenAnswer.add((String) ((TextView)nextChild).getText());
                }
                catch (ClassCastException ignored){

                }
            }
            boolean result = task.checkAnswer(givenAnswer);
            if(result){
                Toast.makeText(getContext(), "Верно!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
            }

        });

        binding.crossButton.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(getActivity(),
                    R.id.nav_host_fragment_container);
            navController.navigate(R.id.taskSelectionFragment);
        });

    }

    public void setTask(OrderTask task){
        this.task = task;
        model.setTask(task);
        binding.phraseToTranslate.setText(task.getTranslatedPhrase());

        bankFlow = addFlow(binding.bankConstraintLayout);
        answerFlow = addFlow(binding.answerConstraintLayout);

        for (String word: task.getVariants()) {
            Button btn = new Button(getContext());
            btn.setId(View.generateViewId());
            btn.setText(word);
            btn.setOnClickListener(new BankOnClickListener());
            binding.bankConstraintLayout.addView(btn);
            bankFlow.addView(btn);
        }
    }

    private Flow addFlow(ConstraintLayout constraintLayout){
        Flow flow = new Flow(getContext());
        flow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        flow.setId(View.generateViewId());
        flow.setWrapMode(Flow.WRAP_CHAIN);
        flow.setHorizontalStyle(Flow.CHAIN_PACKED);
        flow.setHorizontalBias(0f);
        flow.setOrientation(Flow.HORIZONTAL);
        flow.setHorizontalGap(15);
        flow.setVerticalGap(10);
        constraintLayout.addView(flow);
        return flow;
    }



    private void setAnswerUI(List<String> words){
        binding.answerConstraintLayout.removeAllViews();
        for (String word: words) {
            TextView view = (TextView) getLayoutInflater().inflate(R.layout.text_view_order_item, null);
            view.setText(word);
            view.setId(View.generateViewId());
            view.setPadding(0,0,25,0);
            view.setTextSize(24);
            view.setTextColor(getResources().getColor(R.color.black));
            view.setOnClickListener(new AnswerOnClickListener());
            binding.answerConstraintLayout.addView(view);
            answerFlow.addView(view);
        }
        binding.answerConstraintLayout.addView(answerFlow);
    }
    private void setBankUI(List<String> words){
        binding.bankConstraintLayout.removeAllViews();
        for (String word: words) {
            Button view = (Button)getLayoutInflater().inflate(R.layout.item_button, null);
            view.setText(word);
            view.setId(View.generateViewId());

            view.setOnClickListener(new BankOnClickListener());
            binding.bankConstraintLayout.addView(view);
            bankFlow.addView(view);
        }
        binding.bankConstraintLayout.addView(bankFlow);
    }
    class AnswerOnClickListener implements View.OnClickListener {
        public AnswerOnClickListener() {
        }

        @Override
        public void onClick(View view) {
            for (int i = 0; i < binding.answerConstraintLayout.getChildCount(); i++) {
                if(binding.answerConstraintLayout.getChildAt(i) == view){
                    List<String> newValue = model.getAnswer().getValue();
                    newValue.remove(i);
                    ((MutableLiveData)model.getAnswer()).setValue(newValue);
                    break;
                }
            }
        }
    }
    class BankOnClickListener implements View.OnClickListener {
        public BankOnClickListener() {
        }

        @Override
        public void onClick(View view) {
            List<String> newValue = model.getAnswer().getValue();
            newValue.add((String) ((Button)view).getText());
            model.setAnswer(newValue);
        }
    }
}