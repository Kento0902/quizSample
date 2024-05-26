package com.example.quiz.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;

@Controller
@RequestMapping("/quiz")
public class QuizController {
	
	@Autowired
	QuizService service;
	
	/**
	 * form-backing beanの初期化
	 * 
	 * @return 初期化したform
	 */
	@ModelAttribute
	public QuizForm setupForm() {
		QuizForm quizform = new QuizForm();
		
		//ラジオボタンの初期値設定
		quizform.setAnswer(true);
		
		return quizform;
	}
	
	/**
	 * Quizの一覧を表示
	 * 
	 * @return
	 */
	@GetMapping
	public String showList(QuizForm quizform , Model model) {
		
		//新規登録設定
		quizform.setNewQuiz(true);
		
		//掲示板の一覧を取得する
		Iterable<Quiz> list = service.selectAll();
		
		//表示用Modelへの格納
		model.addAttribute("list" , list);
		model.addAttribute("title", "登録用フォーム");
		
		return "crud";
		
	}
	
	/**
	 * Quizデータを1件取得し、フォーム内に表示する
	 * 
	 * @return
	 */
	@GetMapping("/{id}")
	public String showUpdate(
			QuizForm quizForm,
			@PathVariable Integer id,
			Model model) {
		
		//Quizを取得
		Optional<Quiz> quizOutput = service.selectOnebyId(id);
		
		//QuizFormへ詰め直し
		Optional<QuizForm> quizFormOutput = quizOutput.map(t -> makeQuizForm(t));
		
		//QuizFormがnullでなければ中身を取り出す
		if(quizFormOutput.isPresent()) {
			quizForm = quizFormOutput.get();
		}
		
		//更新用のModelを作成する
		makeUpdateModel(quizForm, model);
		
		return "crud";
		
	}
	
	private void makeUpdateModel(QuizForm quizForm, Model model) {
		
		model.addAttribute("id", quizForm.getId());
		quizForm.setNewQuiz(false);
		model.addAttribute("quizForm", quizForm);
		model.addAttribute("title", "更新用フォーム");
		
	}

	/**
	 * Quizデータを1件挿入
	 * 
	 * @return
	 */
	@PostMapping("/insert")
	public String insert(
			@Validated QuizForm quizform,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes
			) {
		
		//FormからEntityへの詰め替え
		Quiz quiz = new Quiz();
		quiz.setQuestion(quizform.getQuestion());
		quiz.setAnswer(quizform.getAnswer());
		quiz.setAuthor(quizform.getAuthor());
		
		//入力チェック
		if(!bindingResult.hasErrors()) {
			service.insertQuiz(quiz);
			redirectAttributes.addFlashAttribute("complete", "登録が完了しました");
			return "redirect:/quiz";
		}else {
			//エラーがある場合は、一覧表示処理を呼びます
			return showList(quizform, model);
		}
		
	}
	
	/**
	 * idをKeyにしてデータを更新する
	 * 
	 * @return
	 */
	@PostMapping("/update")
	public String update(
			@Validated QuizForm quizForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes
			) {
		
		//QuizFormからQuizに詰めなおす
		Quiz quiz = makeQuiz(quizForm);
		
		//入力チェック
		if(!result.hasErrors()) {
			//更新処理、フラッシュスコープの使用、リダイレクト
			service.updateQuiz(quiz);
			redirectAttributes.addFlashAttribute("complete", "更新が完了しました");
			
			//更新画面を表示させる
			return "redirect:/quiz/" + quiz.getId();
		}else {
			//更新用のModelを作成する
			makeUpdateModel(quizForm, model);
			return "crud";
		}
		
	}
	
	/**
	 * QuizFormからQuizに詰めなおして戻り値として返します
	 * 
	 * @param quizForm
	 * @return
	 */
	private Quiz makeQuiz(QuizForm quizForm) {
		
		Quiz quiz = new Quiz();
		quiz.setId(quizForm.getId());
		quiz.setQuestion(quizForm.getQuestion());
		quiz.setAnswer(quizForm.getAnswer());
		quiz.setAuthor(quizForm.getAuthor());
		
		return quiz;
		
	}
	
	/**
	 * QuizからQuizFormに詰めなおして戻り値として返します
	 * 
	 * @param quiz
	 * @return
	 */
	private QuizForm makeQuizForm(Quiz quiz) {
		
		QuizForm form = new QuizForm();
		form.setId(quiz.getId());
		form.setQuestion(quiz.getQuestion());
		form.setAnswer(quiz.isAnswer());
		form.setAuthor(quiz.getAuthor());
		form.setNewQuiz(false);
		
		return form;
		
	}
	
	/**
	 * idをkeyにしてデータを削除する
	 * 
	 * @param id
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("/delete")
	public String delete(
			@RequestParam("id") String id,
			Model model,
			RedirectAttributes redirectAttributes
			) {
		
		//タスクを1件削除してリダイレクト
		service.deleteQuizById(Integer.parseInt(id));
		redirectAttributes.addFlashAttribute("delcomplete", "削除が完了しました");
		
		return "redirect:/quiz";
	}
	
	/**
	 * Quizデータをランダムで１件取得し、画面に表示させる
	 * 
	 * @param quizForm
	 * @param model
	 * 
	 * @return
	 */
	@GetMapping("/play")
	public String showQuiz(QuizForm quizForm, Model model) {
		
		//Quizを取得
		Optional<Quiz> quizOpt = service.selectRandam();
		
		//値が入っているか確認
		if(quizOpt.isPresent()) {
			//QuizFormへ詰め直し
			Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
			quizForm = quizFormOpt.get();
		}else {
			model.addAttribute("msg", "問題がありません");
		}
		
		//表示用「Model」への格納
		model.addAttribute("quizForm", quizForm);
		
		return "play";
		
	}
	
	/**
	 * クイズの正解/不正解を判定する
	 * 
	 * @param quizForm
	 * @param answer
	 * @param model
	 * @return
	 */
	@PostMapping("/check")
	public String checkQuiz(
			QuizForm quizForm,
			@RequestParam Boolean answer,
			Model model
			) {
		
		if(service.checkQuiz(quizForm.getId(), answer)) {
			model.addAttribute("msg", "正解です！");
		}else{
			model.addAttribute("msg", "不正解です・・・");
		}
		
		return "answer";
		
	}

}
