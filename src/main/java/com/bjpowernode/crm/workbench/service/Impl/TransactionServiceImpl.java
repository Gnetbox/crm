package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean saveTran(Tran t) {

        boolean flg = true;
        //创建交易记录
        int count = tranDao.saveTran(t);
        if(count !=1){
            flg = false;
        }

        if(flg){
            //创建一条交易历史
            TranHistory ts = new TranHistory();
            ts.setId(UUIDUtil.getUUID());
            ts.setStage(t.getStage());
            ts.setMoney(t.getMoney());
            ts.setExpectedDate(t.getExpectedDate());
            ts.setCreateBy(t.getCreateBy());
            ts.setCreateTime(DateTimeUtil.getSysTime());
            ts.setTranId(t.getId());

            int count2 = tranHistoryDao.saveTranHis(ts);
            if(count2 !=1){
                flg = false;
            }
        }

        return flg;
    }
}
