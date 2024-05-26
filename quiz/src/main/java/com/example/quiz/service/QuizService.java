package com.example.quiz.service;

import java.util.Optional;

import com.example.quiz.entity.Quiz;

public interface QuizService {
	
	/**
	 * クイズを全件取得
	 * 
	 * @return クイズ情報全件
	 */
	Iterable<Quiz> selectAll();
	
	
	/**
	 * クイズを1件取得
	 * 
	 * @param id クイズのID
	 * @return IDのクイズ1件
	 */
	Optional<Quiz> selectOnebyId(Integer id);
	
	/**
	 * クイズをランダムで1件取得
	 * 
	 * @return ランダムで決められたクイズ1件
	 */
	Optional<Quiz> selectRandam();
	
	/**
	 * クイズの回答の正誤判定
	 * 
	 * @param id クイズのID
	 * @param myAnswer クイズの回答
	 * @return クイズの正解/不正解の判定結果
	 */
	Boolean checkQuiz(Integer id , boolean myAnswer);
	
	/**
	 * クイズを登録
	 * 
	 * @param quiz
	 */
	void insertQuiz(Quiz quiz);
	
	/**
	 * クイズを更新
	 * 
	 * @param quiz
	 */
	void updateQuiz(Quiz quiz);
	
	/**
	 * クイズを削除
	 * 
	 * @param quiz
	 */
	void deleteQuizById(Integer id);

}
