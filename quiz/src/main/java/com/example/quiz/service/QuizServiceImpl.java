package com.example.quiz.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;

@Controller
public class QuizServiceImpl implements QuizService {
	
	//repository注入
	@Autowired
	QuizRepository repository;

	@Override
	public Iterable<Quiz> selectAll() {
		
		return repository.findAll();
	}

	@Override
	public Optional<Quiz> selectOnebyId(Integer id) {
		
		return repository.findById(id);
	}

	@Override
	public Optional<Quiz> selectRandam() {
		
		//ランダムの値を生成する
		Integer randamId = repository.getRandomId();
		
		if(null == randamId) {
			return Optional.empty();
		}
		
		return repository.findById(randamId);
	}

	@Override
	public Boolean checkQuiz(Integer id, boolean myAnswer) {
		
		//クイズの正解/不正解の判定変数
		Boolean check = false;
		
		//対象のクイズを取得
		Optional<Quiz> optQuiz = repository.findById(id);
		
		//値存在チェック
		if(optQuiz.isPresent()) {
			Quiz quiz = optQuiz.get();
			
			//クイズの解答チェック
			if(quiz.isAnswer() == myAnswer) {
				check = true;
			}
		}
		
		return check;
	}

	@Override
	public void insertQuiz(Quiz quiz) {
		repository.save(quiz);
	}

	@Override
	public void updateQuiz(Quiz quiz) {
		repository.save(quiz);
	}

	@Override
	public void deleteQuizById(Integer id) {
		repository.deleteById(id);
	}

}
