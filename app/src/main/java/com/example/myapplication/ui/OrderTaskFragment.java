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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class OrderTaskFragment extends Fragment {
    private FragmentOrderTaskBinding binding;
    private OrderTask task;
    private Flow bankFlow;
    private Flow answerFlow;

    private OrderTaskViewModel model;

    private KonfettiView konfettiView = null;
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

        konfettiView = getActivity().findViewById(R.id.konfetti_view);

        model = new ViewModelProvider(getActivity()).get(OrderTaskViewModel.class);
        model.getAnswer().observe(getViewLifecycleOwner(), a->{
            setAnswerUI(model.getAnswer().getValue());
        });
        model.getBank().observe(getViewLifecycleOwner(), a->{
            setBankUI(model.getBank().getValue());
        });

        model.getTaskLiveData().observe(getViewLifecycleOwner(), a->{
            task = model.getTask();
            binding.phraseToTranslate.setText(task.getTranslatedPhrase());
        });

        if(model.getTask() != null){
            setTask(model.getTask());
        }

        if(model.isRightAnswerBannerActive()){
            binding.rightAnswerBanner.setTranslationY(0);

            activateNextButton(model.isEndOfLesson());
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

                if(Repository.containsLesson(model.getLesson().id)){
                    if(model.getLesson().tasks.size() - 1 > model.getTaskIndex()){
                        model.setTaskIndex(model.getTaskIndex()+1);
                        activateNextButton(false);
                    }
                    else{
                        model.setTaskIndex(model.getTaskIndex()+1);
                        updateProgressBar();
                        model.setEndOfLesson(true);
                        activateNextButton(true);
                        spawnConfetti();
                        Repository.setLessonCompleted(model.getLesson().id);
                    }
                    updateProgressBar();
                    pullRightAnswerBanner();

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

    private void spawnConfetti(){
        parade();
    }

    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.3))
                        .build()
        );
    }

    public void parade() {
        EmitterConfig emitterConfig = new Emitter(1, TimeUnit.SECONDS).perSecond(70);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.RIGHT - 45)
                        .spread(60)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(0.0, 0.5))
                        .build(),
                new PartyFactory(emitterConfig)
                        .angle(Angle.LEFT + 45)
                        .spread(60)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(1.0, 0.5))
                        .build()
        );
    }

    private void setInitialProgressBar(){
        binding.greenBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float maxWidth = binding.barBackdround.getWidth();
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
        model.setRightAnswerBannerActive(true);
    }

    private void closeRightAnswerBanner(){
        ObjectAnimator animation = ObjectAnimator.ofFloat(binding.rightAnswerBanner, "translationY", 0,500);
        animation.setDuration(300);
        animation.start();
        model.setRightAnswerBannerActive(false);
    }

    private void activateNextButton(boolean isEndOfLesson){
            if(!isEndOfLesson){
                binding.nextTaskButton.setText(R.string.next);
                binding.rightAnswerBanner.setBackgroundColor(getResources().getColor(R.color.green));
                binding.nextTaskButton.setEnabled(true);
                binding.nextTaskButton.setOnClickListener(view2 -> {
                    model.setTask(model.getLesson().tasks.get(model.getTaskIndex()));
                    binding.nextTaskButton.setEnabled(false);
                    closeRightAnswerBanner();
                });
            }
            else{
                binding.nextTaskButton.setEnabled(true);
                binding.nextTaskButton.setText(R.string.finish_lesson);
                binding.rightAnswerBanner.setBackgroundColor(getResources().getColor(R.color.green));
                binding.nextTaskButton.setOnClickListener(view2 -> {
                    NavController navController = Navigation.findNavController(getActivity(),
                            R.id.nav_host_fragment_container);
                    model.setOnTaskScreen(false);
                    navController.navigate(R.id.action_orderTaskFragment_to_taskSelectionFragment);
                    binding.nextTaskButton.setEnabled(false);
                    closeRightAnswerBanner();
                });
            }
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
        executorService.shutdownNow();
    }
}