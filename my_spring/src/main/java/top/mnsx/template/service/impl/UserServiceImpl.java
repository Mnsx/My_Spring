package top.mnsx.template.service.impl;

import top.mnsx.my_spring.annotation.Transactional;
import top.mnsx.template.dao.UserDao;
import top.mnsx.template.service.UserService;
import top.mnsx.my_spring.annotation.Autowired;
import top.mnsx.my_spring.annotation.bean.Service;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.template.service.impl
 * @CreateTime: 2022/7/16
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Integer transfer(int in, int out, int money) {
        Integer inMoney = userDao.getMoneyById(in);
        Integer res1 = userDao.updateBalanceById(in, inMoney + money);
        int i = 1 / 0;
        Integer outMoney = userDao.getMoneyById(out);
        Integer res2 = userDao.updateBalanceById(out, outMoney - money);
        return res1 * res2;
    }
}
