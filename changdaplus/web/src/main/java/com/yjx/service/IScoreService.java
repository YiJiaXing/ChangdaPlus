package com.yjx.service;

import com.yjx.pojo.Score;

public interface IScoreService {
	
	// 添加成绩
	public int addScore(Score score);

	// 修改成绩
	public int updateScore(Score score);

	// 查询成绩
	public Score getScore(String no);

}
