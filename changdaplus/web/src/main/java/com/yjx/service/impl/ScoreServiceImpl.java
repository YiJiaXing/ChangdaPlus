package com.yjx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yjx.dao.IScoreDao;
import com.yjx.pojo.Score;
import com.yjx.service.IScoreService;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class ScoreServiceImpl implements IScoreService {

	@Autowired
	IScoreDao scoreDao;
	
	public int addScore(Score score) {
		// TODO Auto-generated method stub
		return scoreDao.addScore(score);
	}

	public int updateScore(Score score) {
		// TODO Auto-generated method stub
		return scoreDao.updateScore(score);
	}

	public Score getScore(String no) {
		// TODO Auto-generated method stub
		return scoreDao.getScore(no);
	}

}
