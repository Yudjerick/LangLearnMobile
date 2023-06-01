package com.example.myapplication.ui;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.OrderTask;
import com.example.myapplication.data.Repository;
import com.example.myapplication.viewmodels.AnswerItem;
import com.example.myapplication.viewmodels.BankItem;
import com.example.myapplication.viewmodels.OrderTaskViewModel;
import com.example.myapplication.databinding.FragmentOrderTaskBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrderTaskFragment extends Fragment {
    private FragmentOrderTaskBinding binding;
    private OrderTask task;
    private Flow bankFlow;
    private Flow answerFlow;

    private OrderTaskViewModel model;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
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



        model.getTaskLiveData().observe(getViewLifecycleOwner(), a->{
            task = model.getTask();
            binding.phraseToTranslate.setText(task.getTranslatedPhrase());
            //updateProgressBar();
        });





        if(model.getTask() != null){
            setTask(model.getTask());
        }

        binding.checkButton.setOnClickListener(view1 -> {
            ArrayList<String> givenAnswer = new ArrayList<>();
            for(int index = 0; index < ((ViewGroup) binding.answerConstraintLayout).getChildCount(); index++) {
                View nextChild = ((ViewGroup) binding.answerConstraintLayout).getChildAt(index);
                try {
                    givenAnswer.add((String) ((TextView)nextChild).getText());
                }
                catch (ClassCastException ignored){}
            }
            boolean result = task.checkAnswer(givenAnswer);
            if(result){
                binding.robotCharacter.setImageResource(R.drawable.anim_robot_right);
                AnimatedVectorDrawable avd = (AnimatedVectorDrawable) binding.robotCharacter.getDrawable();
                avd.start();

                pullRightAnswerBanner();

                if(Repository.containsLesson(model.getLesson().id)){
                    if(model.getLesson().tasks.size() - 1 > model.getTaskIndex()){
                        model.setTaskIndex(model.getTaskIndex()+1);
                        binding.nextTaskButton.setEnabled(true);
                        updateProgressBar();
                        binding.nextTaskButton.setOnClickListener(view2 -> {
                            model.setTask(model.getLesson().tasks.get(model.getTaskIndex()));
                            binding.nextTaskButton.setEnabled(false);
                            closeRightAnswerBanner();
                        });
                    }
                    else{
                        model.setTaskIndex(model.getTaskIndex()+1);
                        updateProgressBar();
                        Repository.setLessonCompleted(model.getLesson().id);
                    }
                }
            }
            else{
                binding.robotCharacter.setImageResource(R.drawable.anim_robot_wrong);
                AnimatedVectorDrawable avd = (AnimatedVectorDrawable) binding.robotCharacter.getDrawable();
                avd.start();
            }

        });

        binding.crossButton.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(getActivity(),
                    R.id.nav_host_fragment_container);
            model.setOnTaskScreen(false);
            navController.navigate(R.id.action_orderTaskFragment_to_taskSelectionFragment);
        });
        setInitialProgressBar();

    }

    private void setInitialProgressBar(){
        binding.greenBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float maxWidth = binding.barBackdround.getWidth();
                model.setGreenBarMaxWidth(maxWidth);
                float ratio = (float) model.getTaskIndex()/(float) model.getLesson().tasks.size();
                binding.greenBar.requestLayout();
                binding.greenBar.getLayoutParams().width = (int) (ratio* maxWidth);

                binding.greenBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
    private void updateProgressBar(){
        if(!binding.greenBar.isLaidOut()){
            return;
        }
        float maxWidth = binding.barBackdround.getWidth();
        model.setGreenBarMaxWidth(maxWidth);
        float ratio = (float) model.getTaskIndex()/(float) model.getLesson().tasks.size();
        binding.greenBar.requestLayout();

        Runnable greenBarUpdating = new Runnable() {
            @Override
            public void run() {
                int previousWidth = binding.greenBar.getWidth();
                try {
                    for (int i = 0; i < 9; i++) {
                        Thread.sleep(20);
                        binding.greenBar.post(() -> {
                            binding.greenBar.requestLayout();
                            binding.greenBar.getLayoutParams().width += (int)( ((maxWidth *
                                    ratio - previousWidth)) / 10);
                        });
                    }
                    Thread.sleep(20);
                    binding.greenBar.post(() -> {
                        binding.greenBar.requestLayout();
                        binding.greenBar.getLayoutParams().width = (int) (maxWidth*ratio);
                    });

                } catch (InterruptedException e) {
                    binding.greenBar.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.greenBar.getLayoutParams().width = (int) (maxWidth*ratio);
                        }
                    });
                }
            }
        };
        executorService.execute(greenBarUpdating);

    }

    private void pullRightAnswerBanner(){
        ObjectAnimator animation = ObjectAnimator.ofFloat(binding.rightAnswerBanner, "translationY", 500,0);
        animation.setDuration(300);
        animation.start();
    }

    private void closeRightAnswerBanner(){
        ObjectAnimator animation = ObjectAnimator.ofFloat(binding.rightAnswerBanner, "translationY", 0,500);
        animation.setDuration(300);
        animation.start();
    }

    public void setTask(OrderTask task){
        this.task = task;
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
    private void setAnswerUI(List<AnswerItem> answerItems){
        binding.answerConstraintLayout.removeAllViews();
        for (AnswerItem answerItem: answerItems) {
            TextView view = (TextView) getLayoutInflater().inflate(R.layout.text_view_order_item, null);
            view.setText(answerItem.getWord());
            view.setId(View.generateViewId());
            view.setOnClickListener(new AnswerOnClickListener());
            binding.answerConstraintLayout.addView(view);
            answerFlow.addView(view);
        }
        binding.answerConstraintLayout.addView(answerFlow);
    }
    private void setBankUI(List<BankItem> bankItems){
        binding.bankConstraintLayout.removeAllViews();
        for (BankItem bankItem: bankItems) {
            Button view;
            if(bankItem.isActive()){
                view = (Button)getLayoutInflater().inflate(R.layout.item_button, null);
            }else {
                view = (Button)getLayoutInflater().inflate(R.layout.item_button_disabled, null);
            }
            view.setText(bankItem.getWord());
            view.setId(View.generateViewId());
            view.setOnClickListener(new BankOnClickListener());
            binding.bankConstraintLayout.addView(view);
            bankFlow.addView(view);

            if(!bankItem.isActive()){
                view.setEnabled(false);
            }
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
                    List<AnswerItem> newValue = model.getAnswer().getValue();
                    List<BankItem> newBank = model.getBank().getValue();


                    newValue.get(i).getAssociated().setActive(true);
                    newValue.remove(i);
                    model.setAnswer(newValue);

                    model.setBank(newBank);
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
            int buttonIndex = -1;
            for (int i = 0; i < binding.bankConstraintLayout.getChildCount(); i++) {
                if(binding.bankConstraintLayout.getChildAt(i) == view){
                    buttonIndex = i;
                    break;
                }
            }

            List<AnswerItem> newValue = model.getAnswer().getValue();
            List<BankItem> newBank = model.getBank().getValue();
            newValue.add(new AnswerItem(((Button)view).getText().toString(), newBank.get(buttonIndex)));
            model.setAnswer(newValue);

            newBank.get(buttonIndex).setActive(false);
            model.setBank(newBank);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        model.setShouldCallOnPreDrawListener(true);
        executorService.shutdownNow();
    }
}