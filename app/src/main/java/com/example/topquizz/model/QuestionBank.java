package com.example.topquizz.model;

import java.util.Collections;
import java.util.List;

public class QuestionBank
{
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public QuestionBank(List<Question> questionList)
    {
        mQuestionList = questionList;

        //Mélange la liste des questions avant de la stocker
        Collections.shuffle(mQuestionList);

        mNextQuestionIndex = 0;
    }

    public Question getQuestion()
    { // Boucle dans les questions et en renvoie une nouvelle à chaque passage

        if (mNextQuestionIndex == mQuestionList.size())
        {
            mNextQuestionIndex = 0;
        }

        return mQuestionList.get(mNextQuestionIndex++);

    }

}
