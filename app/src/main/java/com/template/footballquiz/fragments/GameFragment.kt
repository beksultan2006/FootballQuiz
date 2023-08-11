package com.template.footballquiz.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.template.footballquiz.R
import com.template.footballquiz.databinding.FragmentGameBinding
import com.template.footballquiz.model.Constants
import com.template.footballquiz.model.Question

class GameFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentGameBinding
    private lateinit var mQuestionsList: ArrayList<Question>
    private var mSelectedPosition: Int = 0
    private var mCorrectAnswer: Int = 0
    var mCurrrentPosition: Int = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        mQuestionsList = Constants.getQuestion()
        Log.i("info", mCurrrentPosition.toString())
        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        setQuestion()
        return binding.root
    }

    private fun setQuestion() {
        val question: Question = mQuestionsList[mCurrrentPosition - 1]
        binding.tvQuestion.text = question.question
        binding.imageView.setImageResource(question.image)
        binding.tvOptionOne.text = question.optionOne
        binding.tvOptionTwo.text = question.optionTwo
        binding.tvOptionThree.text = question.optionThree
        binding.tvOptionFour.text = question.optionFour
        binding.pb.progress = mCurrrentPosition
        binding.tvProgress.text =
            "$mCurrrentPosition" + "/" + binding.pb.max
        defaultAppearance()
        if (mCurrrentPosition == mQuestionsList.size) {
            binding.btnSubmit.text = "Quiz Finished"
        } else {
            binding.btnSubmit.text = "Submit"
        }
    }

    private fun defaultAppearance() {
        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)
        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.default_option_border_bg) }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_optionOne -> {
                selectedOptionView(binding.tvOptionOne, 1)
            }
            R.id.tv_optionTwo -> {
                selectedOptionView(binding.tvOptionTwo, 2)
            }
            R.id.tv_optionThree -> {
                selectedOptionView(binding.tvOptionThree, 3)
            }
            R.id.tv_optionFour -> {
                selectedOptionView(binding.tvOptionFour, 4)
            }
            R.id.btnSubmit -> {
                if (mSelectedPosition == 0) {
                    mCurrrentPosition++
                    when {
                        mCurrrentPosition <= mQuestionsList.size -> {
                            setQuestion()
                        }
                        else -> {
                            val action = GameFragmentDirections.actionGameFragmentToScoreFragment()
                            val nameOfPlayer by navArgs<GameFragmentArgs>()
                            action.score = mCorrectAnswer
                            action.name = nameOfPlayer.name
                            findNavController().navigate(action)
                        }
                    }
                } else {
                    val question = mQuestionsList[mCurrrentPosition - 1]
                    if (question.correctAnswer != mSelectedPosition) {
                        answerView(mSelectedPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswer++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    if (mCurrrentPosition == mQuestionsList.size) {
                        binding.btnSubmit.text = "Finished"
                    } else {
                        binding.btnSubmit.text = " Go to Next Question"
                    }
                    mSelectedPosition = 0
                }
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedPosition: Int) {
        defaultAppearance()
        mSelectedPosition = selectedPosition
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.selected_option_border_bg) }
    }

    private fun answerView(mSelectedPosition: Int, drawableView: Int) {
        when (mSelectedPosition) {
            1 -> {
                binding.tvOptionOne.background =
                    context?.let { ContextCompat.getDrawable(it, drawableView) }
            }
            2 -> {
                binding.tvOptionTwo.background =
                    context?.let { ContextCompat.getDrawable(it, drawableView) }
            }
            3 -> {
                binding.tvOptionThree.background =
                    context?.let { ContextCompat.getDrawable(it, drawableView) }
            }
            4 -> {
                binding.tvOptionFour.background =
                    context?.let { ContextCompat.getDrawable(it, drawableView) }
            }
        }
    }
}