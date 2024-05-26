package com.example.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.quiz.entity.Quiz;
import com.example.quiz.service.QuizService;

@SpringBootApplication
public class QuizApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizApplication.class, args)
		.getBean(QuizApplication.class).execute();
	}
	
	@Autowired
	QuizService service;

	/**
	 * 実行メソッド
	 */
	private void execute() {
		//登録処理
//		setup();
		
		//全件取得
//		showList();
//		
//		//1件取得
//		showOne();
//		
//		//更新処理
//		updateQuiz();
//		
//		//削除処理
//		deleteQuiz();
		
//		doQuiz();
	}


	/**
	 * クイズを2件登録
	 */
	private void setup() {
		
		System.out.println("ーーー登録処理開始ーーー");
		
		//エンティティ作成
		Quiz quiz1 = new Quiz(null
				,"javaはオブジェクト指向言語である",
				true,
				"登録太郎");
		
		Quiz quiz2 = new Quiz(null
				,"Spring Dataはデータアクセスに対する機能を提供する",
				true,
				"登録太郎");
		
		Quiz quiz3 = new Quiz(null
				,"プログラムがたくさん配置されているサーバーのことを「ライブラリ」という",
				false,
				"登録太郎");
		
		Quiz quiz4 = new Quiz(null
				,"「@Component」はインスタンス生成アノテーションである",
				true,
				"登録太郎");
		
		Quiz quiz5 = new Quiz(null
				,"すべてのリクエストを１つのコントローラーで受け取るパターンは"
				 + "「シングルコントローラ・パターン」である。",
				false,
				"登録太郎");
		
		//リストにエンティティを格納
		List<Quiz> quizList = new ArrayList<>();
		
		Collections.addAll(quizList, quiz1, quiz2, quiz3, quiz4, quiz5);
		
		//登録実行
		for(Quiz quiz : quizList) {
			service.insertQuiz(quiz);
		}
		
		System.out.println("ーーー登録完了ーーー");
		
	}

	
	private void showList() {
		System.out.println("ーーー全件取得開始ーーー");
		
		Iterable<Quiz> quizzes = service.selectAll();
		for(Quiz quiz : quizzes) {
			System.out.println(quiz);
		}
		
		System.out.println("ーーー全件取得完了ーーー");
	}
	

	private void showOne() {
		System.out.println("ーーー１件取得開始ーーー");
		
		Optional<Quiz> quizOpt = service.selectOnebyId(1);
		//値存在チェック
		if(quizOpt.isPresent()) {
			System.out.println(quizOpt.get());
		}else {
			System.out.println("該当する問題が見つかりません。");
		}
		
		System.out.println("ーーー１件取得完了ーーー");
	}
	
	private void updateQuiz() {
		System.out.println("ーーー更新処理開始ーーー");
		
		Quiz quiz1 = new Quiz(
				1,
				"「スプリング」はフレームワークですか？",
				true,
				"変更タロウ"
				);
		
		service.updateQuiz(quiz1);
		
		System.out.println("更新したデータは" + quiz1 + "です。");
		
		System.out.println("ーーー更新処理完了ーーー");
	}
	

	private void deleteQuiz() {
		System.out.println("ーーー削除処理開始ーーー");
		
		service.deleteQuizById(2);

		System.out.println("ーーー削除処理終了ーーー");
	}
	
	
	/**
	 * ランダムでクイズを取得して、クイズの正解/不正解を判定する
	 */
	private void doQuiz() {
		
		System.out.println("ーーークイズ1件取得開始ーーー");
		
		Optional<Quiz> quizOutput = service.selectRandam();
		
		//値存在チェック
		if(quizOutput.isPresent()) {
			System.out.println(quizOutput.get());
		}else {
			System.out.println("該当する問題が存在しません");
		}
		
		System.out.println("ーーークイズ1件取得完了ーーー");
		
		//解答を実施
		Boolean myAnswer = false;
		
		Integer id = quizOutput.get().getId();
		
		if(service.checkQuiz(id, myAnswer)) {
			System.out.println("正解です！");
		}else {
			System.out.println("不正解です！");
		}
	}
}
