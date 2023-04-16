package com.example.myapplication;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.databinding.FragmentOrderTaskBinding;

import java.util.ArrayList;

public class OrderTaskFragment extends Fragment {
    private FragmentOrderTaskBinding binding;

    private OrderTask task;

    private Flow bankFlow;
    private Flow targetFlow;
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

        String s = "Он обещал закончить проект через неделю";
        String[] words = {"He","promised","to", "finish", "the", "project", "in", "one", "week"};
        OrderTask task1 = new OrderTask(s,words);
        setTask(task1);

        binding.checkButton.setOnClickListener(view1 -> {
            ArrayList<String> givenAnswer = new ArrayList<>();
            for(int index = 0; index < ((ViewGroup) binding.targetConstraintLayout).getChildCount(); index++) {
                View nextChild = ((ViewGroup) binding.targetConstraintLayout).getChildAt(index);
                try {
                    givenAnswer.add((String) ((Button)nextChild).getText());
                }
                catch (ClassCastException ignored){

                }

            }
            for (String w:givenAnswer
            ) {
                Log.d("AAAA", w);
            }
            for (String w:task.getAnswer()
            ) {
                Log.i("AAAA", w);
            }
            Toast.makeText(getContext(), String.valueOf(task.checkAnswer(givenAnswer)), Toast.LENGTH_SHORT).show();
        });
    }

    public void setTask(OrderTask task){
        this.task = task;
        binding.phraseToTranslate.setText(task.getTranslatedPhrase());
        //String[] words = {"He","promised","to", "finish", "the", "project", "in", "one", "week"};

        bankFlow = addFlow(binding.bankConstraintLayout);
        targetFlow = addFlow(binding.targetConstraintLayout);

        for (String word: task.getVariants()) {
            Button btn = new Button(getContext());
            btn.setId(View.generateViewId());
            btn.setText(word);
            btn.setOnClickListener(new MoveFromBankOnClickListener());
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
        constraintLayout.addView(flow);
        return flow;
    }

    class MoveFromBankOnClickListener implements View.OnClickListener {
        public MoveFromBankOnClickListener() {
        }

        @Override
        public void onClick(View view) {
            binding.bankConstraintLayout.removeView(view);
            bankFlow.removeView(view);
            binding.targetConstraintLayout.addView(view);
            targetFlow.addView(view);
            view.setOnClickListener(new MoveToBankOnClickListener());
        }
    }

    class MoveToBankOnClickListener implements View.OnClickListener {
        public MoveToBankOnClickListener() {
        }

        @Override
        public void onClick(View view) {
            binding.targetConstraintLayout.removeView(view);
            targetFlow.removeView(view);
            binding.bankConstraintLayout.addView(view);
            bankFlow.addView(view);
            view.setOnClickListener(new MoveFromBankOnClickListener());
        }
    }
}