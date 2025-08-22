package com.fengpei.web.controller;


import com.fengpei.web.entiry.*;
import com.fengpei.web.tool.BusinessTool;
import com.fengpei.web.tool.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
public class HelloController {
    @Autowired
    private DataSource dataSource;
    private final static String TABLE_NAME = "fengpei.clients";
    private final static String NULL_PARAMETER = "请求参数不能为空";
    private final DataDao dataDao = new DataDao();
    public final BusinessTool businessTool = new BusinessTool();


    @GetMapping("/hello")
    public String hello() {
        return "helloWord";
    }

    @GetMapping("/resultData-tes")
    public BaseDataResult resultData(@RequestParam(value = "test", required = false) String test) {
        BaseDataResult success = new BaseDataResult();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM + " + TABLE_NAME + " where name like";
            String regexPattern = "\"%" + test + "%\""; // 不区分大小写匹配
            ResultSet resultSet = statement.executeQuery(sql + regexPattern);
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Integer age = resultSet.getInt("age");
                Integer gender = resultSet.getInt("gender");
                success.setCode(1);
                success.setMsg("请求成功");
                //success.setData(new Client(id, name, age, gender));
            }
            closeResource(statement, connection);
        } catch (SQLException e) {
            success.setCode(0);
            success.setMsg("请求失败");
            throw new RuntimeException(e);
        }
        return success;
    }

    /**
     * 根据id查找客户信息
     */
    @PostMapping("/getClientById-tes")
    public BaseDataResult getClientById(Integer clientId) {
        BaseDataResult success = new BaseDataResult();
        if (clientId == null) {
            success.code = 1;
            success.msg = NULL_PARAMETER;
            return success;
        }
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + TABLE_NAME + " where id =";
            ResultSet resultSet = statement.executeQuery(sql + clientId);
            try {
                if (resultSet.next()) {
                    Client client = businessTool.setClientData(resultSet);
                    success.setData(client);
                    success.setCode(1);
                    success.setMsg("请求成功");
                } else {
                    success.setCode(0);
                    success.setMsg("没有数据");
                }
            } catch (Exception e) {
                success.setCode(0);
                success.setMsg("丰沛:getUserById解析数据异常");

            }
            closeResource(statement, connection);
        } catch (SQLException e) {
            success.setCode(0);
            success.setMsg("丰沛:getUserById数据库操作失败");
        }
        return success;
    }

    /**
     * 根据id查找客户信息
     */
    @PostMapping("/getClientByName-tes")
    public BaseData getClientByName(String text) {
        if (text == null || text.isEmpty()) {
            BaseData baseData = new BaseData();
            baseData.code = 1;
            baseData.msg = NULL_PARAMETER;
            return baseData;
        }
        BaseDataListClient success = new BaseDataListClient();
        List<Client> clientList = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + TABLE_NAME + " where clientName like";
            String regexPattern = "\"%" + text + "%\""; // 不区分大小写匹配
            ResultSet resultSet = statement.executeQuery(sql + regexPattern);
            try {
                while (resultSet.next()) {
                    Client client = businessTool.setClientData(resultSet);
                    clientList.add(client);
                }
                success.setCode(1);
                success.setMsg("请求成功");
            } catch (Exception e) {
                success.setCode(0);
                success.setMsg("丰沛:getClientByName解析数据异常");
            }
            closeResource(statement, connection);
        } catch (SQLException e) {
            success.setCode(0);
            success.setMsg("丰沛:getClientByName数据库操作失败");
            throw new RuntimeException(e);
        }
        success.setData(clientList);
        return success;
    }

    /**
     * 获取客户列表
     */
    @PostMapping("/getAllClientList")
    public BaseDataListClient getAllClientList(Integer page) {
        // 这里可以添加业务逻辑，例如保存用户到数据库
        BaseDataListClient success = new BaseDataListClient();
        List<Client> clientList = new ArrayList<>();
        if (page == null) {
            success.code = 1;
            success.msg = NULL_PARAMETER;
            success.setData(clientList);
            return success;
        }
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            int limitNum = 20;
            String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC " + " LIMIT " + limitNum + " OFFSET " + (page - 1) * limitNum;
            ResultSet resultSet = statement.executeQuery(sql);
            try {
                while (resultSet.next()) {
                    Client client = businessTool.setClientData(resultSet);
                    clientList.add(client);
                }
                success.setCode(1);
                success.setMsg("请求成功");
            } catch (Exception e) {
                success.setCode(0);
                success.setMsg("丰沛:getUserList解析数据异常");
            }
            closeResource(statement, connection);
        } catch (SQLException e) {
            success.setCode(0);
            success.setMsg("丰沛:getUserList数据库操作失败");
        }
        success.setData(clientList);
        return success;
    }


    /**
     * 获取客户列表
     */
    @PostMapping("/getSpacialClientList")
    public BaseDataListClient getSpacialClientList(Integer page) {
        // 这里可以添加业务逻辑，例如保存用户到数据库
        BaseDataListClient success = new BaseDataListClient();
        List<Client> clientList = new ArrayList<>();
        if (page == null) {
            success.code = 1;
            success.msg = NULL_PARAMETER;
            success.setData(clientList);
            return success;
        }
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            int limitNum = 20;
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE status != 0 " + " ORDER BY id DESC " + " LIMIT " + limitNum + " OFFSET " + (page - 1) * limitNum;
            ResultSet resultSet = statement.executeQuery(sql);
            try {
                while (resultSet.next()) {
                    Client client = businessTool.setClientData(resultSet);
                    clientList.add(client);
                }
                success.setCode(1);
                success.setMsg("请求成功");
            } catch (Exception e) {
                success.setCode(0);
                success.setMsg("丰沛:getUserList解析数据异常");
            }
            closeResource(statement, connection);
        } catch (SQLException e) {
            success.setCode(0);
            success.setMsg("丰沛:getUserList数据库操作失败");
        }
        success.setData(clientList);
        return success;
    }

    /**
     * 删除客户
     */
    @PostMapping("/deleteClient-tes")
    public BaseData deleteClient(Integer clientId) {
        BaseData baseData = new BaseData();
        if (clientId == null) {
            baseData.code = 1;
            baseData.msg = NULL_PARAMETER;
            return baseData;
        }
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String str = " WHERE id = " + clientId;
            String sql = "DELETE FROM " + TABLE_NAME + str;
            int code = statement.executeUpdate(sql);
            if (code == 1) {
                baseData.code = 1;
                baseData.msg = "删除成功";
            } else {
                baseData.code = 0;
                baseData.msg = "deleteClient删除数据异常码:" + code;
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            baseData.msg = "丰沛:deleteClient删除客户失败";
            baseData.code = 0;
        }
        return baseData;
    }

    /**
     * 更改客户信息(通过/再次补充)
     */
    @PostMapping("/modifyClientData")
    public BaseData modifyClientData(Integer clientId, Integer status) {
        BaseData baseData = new BaseData();
        if (clientId == null || status == null) {
            baseData.code = 1;
            baseData.msg = NULL_PARAMETER;
            return baseData;
        }
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            //先查询ID
            String sqlInquire = "SELECT * FROM " + TABLE_NAME + " where id =";
            ResultSet resultSet = statement.executeQuery(sqlInquire + clientId);
            if (resultSet.next()) {
                Client client = businessTool.setClientData(resultSet);
                if (client.status == 0) {
                    baseData.code = 1;
                    baseData.msg = "该用户还没有补交资料，你不能更改";
                    return baseData;
                } else if (client.status == 2) {
                    baseData.code = 1;
                    baseData.msg = "该用户已经通过审核了，你不能更改";
                    return baseData;
                } else if (client.status == 3) {
                    baseData.code = 1;
                    baseData.msg = "该用户已经被拒绝了，你不能更改";
                    return baseData;
                }
            }
            String str = " SET status = " + status + " WHERE id=" + clientId;
            String sql = "UPDATE " + TABLE_NAME + str;
            int code = statement.executeUpdate(sql);
            if (code == 1) {
                baseData.code = 1;
                baseData.msg = "修改成功";
            } else {
                baseData.code = 0;
                baseData.msg = "modifyClientData修改数据异常码:" + code;
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            baseData.msg = "丰沛:modifyClientData修改客户失败";
            baseData.code = 0;
        }
        return baseData;
    }

    /**
     * 更改客户信息(通过/再次补充)
     */
    @PostMapping("/modifyRefuseClientData")
    public BaseData modifyRefuseClientData(Integer clientId, String refuseReasonOne, String refuseReasonTwo) {
        BaseData baseData = new BaseData();
        if (clientId == null || refuseReasonOne == null || refuseReasonOne.isEmpty() || refuseReasonTwo == null || refuseReasonTwo.isEmpty()) {
            baseData.code = 1;
            baseData.msg = NULL_PARAMETER;
            return baseData;
        }
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            //先查询ID
            String sqlInquire = "SELECT * FROM " + TABLE_NAME + " where id =";
            ResultSet resultSet = statement.executeQuery(sqlInquire + clientId);
            if (resultSet.next()) {
                Client client = businessTool.setClientData(resultSet);
                if (client.status == 0) {
                    baseData.code = 1;
                    baseData.msg = "该用户还没有补交资料，你不能更改";
                    return baseData;
                } else if (client.status == 2) {
                    baseData.code = 1;
                    baseData.msg = "该用户已经通过审核了，你不能更改";
                    return baseData;
                } else if (client.status == 3) {
                    baseData.code = 1;
                    baseData.msg = "该用户已经被拒绝了，你不能更改";
                    return baseData;
                }
            }
            String str = " SET status = " + 3 + ", refuseReasonOne = " + "'" + refuseReasonOne + "'" + ", refuseReasonTwo = " + "'" + refuseReasonTwo + "'" + " WHERE id=" + clientId;
            String sql = "UPDATE " + TABLE_NAME + str;
            int code = statement.executeUpdate(sql);
            if (code == 1) {
                baseData.code = 1;
                baseData.msg = "修改成功";
            } else {
                baseData.code = 0;
                baseData.msg = "modifyClientData修改数据异常码:" + code;
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            baseData.msg = "丰沛:modifyClientData修改客户失败";
            baseData.code = 0;
        }
        return baseData;
    }

    public void closeResource(Statement statement, Connection connection) throws SQLException {
        statement.close();
        connection.close();
    }

    /**
     * 添加数据
     */
    @PostMapping("/calculating")
    public BaseDataCalculating calculating(String clientName, String city, String phone, String identityCard, String bankId, Integer annualIncome, Integer socialSecurity, Integer accumulationFund, Integer estateValue, Integer loanAmount, Integer antPoints, Integer creditQuery, Integer type) {
        estateValue = Optional.ofNullable(estateValue).orElse(0);
        socialSecurity = Optional.ofNullable(socialSecurity).orElse(0);
        accumulationFund = Optional.ofNullable(accumulationFund).orElse(0);
        BaseDataCalculating success = new BaseDataCalculating();
        double quantityNumber;
        if (socialSecurity.equals(accumulationFund)) {
            quantityNumber = socialSecurity;
        } else {
            quantityNumber = accumulationFund > socialSecurity ? accumulationFund : socialSecurity;
        }
        double estate = estateValue;
        double quantity = businessTool.countingOneStep((double) annualIncome, quantityNumber, loanAmount, estate);
        int result = businessTool.countingTwoStep(antPoints, creditQuery, quantity);
        long clientId = 0;
        //数据入库
        if (result > 0) {
            Client client = new Client();
            client.clientName = clientName;
            client.city = city;
            client.phone = phone;
            client.identityCard = identityCard;
            client.monthIncome = annualIncome;
            client.socialSecurity = socialSecurity;
            client.accumulationFund = accumulationFund;
            client.estateValue = estateValue;
            client.loanAmount = loanAmount;
            client.antPoints = antPoints;
            client.creditStatue = creditQuery;
            client.bankId = bankId;
            client.type = type;
            client.assessMoney = result;
            client.status = 0;
            client.applyTime = businessTool.formalTool.getCurrentTime();
            clientId = dataDao.addClientData(client, dataSource);
            if (clientId < 1) {
                success.code = 0;
                success.msg = "input失败";
                return success;
            }
        }
        success.code = 1;
        success.msg = "获取额度成功";
        success.setData(businessTool.setSelectDataContent(result, type, bankId, identityCard, clientId));
        return success;
    }


    /**
     * 添加数据
     */
    @PostMapping("/getCalculateDate")
    public BaseDataCalculating getCalculateDate(int assessMoney, String bankId, String identityCard, int clientId, Integer type) {
        BaseDataCalculating success = new BaseDataCalculating();
        success.code = 1;
        success.msg = "获取数据成功";
        success.setData(businessTool.setSelectDataContent(assessMoney, type, bankId, identityCard, clientId));
        return success;
    }

    @PostMapping("/updateData")
    public BaseData updateData(int clientId, String educationBackground, String maritalStatus, String debt, String presentAddress, String detailAddress, String livingModel, int livingSpend, String childrenNumber, String relativeOneName, String relativeOneBetween, String relativeOnePhone, String relativeTwoName, String relativeTwoBetween, String relativeTwoPhone, String colleagueOneName, String colleagueOnePhone, String colleagueTwoName, String colleagueTwoPhone, String companyname, String companytype, String companysector, String companyposition, String companytime, String leaderName, String companyScale, String monthSalary, String acquairSalaryType, String acquairSalaryDate, String companyAdress, String companyPhoneNumber, String commuteTime, String remark) {
        BaseData success = new BaseData();
        String currentTime = businessTool.formalTool.getCurrentTime();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String str = " SET" + " educationBackground = " + "'" + educationBackground + "'" + ", maritalStatus = " + "'" + maritalStatus + "'" + ", debt = " + "'" + debt + "'" + ", presentAddress = " + "'" + presentAddress + "'" + ", detailAddress = " + "'" + detailAddress + "'" + ", livingModel = " + "'" + livingModel + "'" + ", livingSpend = " + livingSpend + ", childrenNumber = " + "'" + childrenNumber + "'" + ", relativeOneName = " + "'" + relativeOneName + "'" + ", relativeOneBetween = " + "'" + relativeOneBetween + "'" + ", relativeOnePhone = " + "'" + relativeOnePhone + "'" + ", relativeTwoName = " + "'" + relativeTwoName + "'" + ", relativeTwoBetween = " + "'" + relativeTwoBetween + "'" + ", relativeTwoPhone = " + "'" + relativeTwoPhone + "'" + ", colleagueOneName = " + "'" + colleagueOneName + "'" + ", colleagueOnePhone = " + "'" + colleagueOnePhone + "'" + ", colleagueTwoName = " + "'" + colleagueTwoName + "'" + ", colleagueTwoPhone = " + "'" + colleagueTwoPhone + "'" + ", companyname = " + "'" + companyname + "'" + ", companytype = " + "'" + companytype + "'" + ", companysector = " + "'" + companysector + "'" + ", companyposition = " + "'" + companyposition + "'" + ", companytime = " + "'" + companytime + "'" + ", leaderName = " + "'" + leaderName + "'" + ", companyScale = " + "'" + companyScale + "'" + ", monthSalary = " + "'" + monthSalary + "'" + ", acquairSalaryType = " + "'" + acquairSalaryType + "'" + ", acquairSalaryDate = " + "'" + acquairSalaryDate + "'" + ", companyAdress = " + "'" + companyAdress + "'" + ", companyPhoneNumber = " + "'" + companyPhoneNumber + "'" + ", commuteTime = " + "'" + commuteTime + "'" + ", remark = " + "'" + remark + "'" + ", submitTime = " + "'" + currentTime + "'" + ", status = " + 1 + " WHERE id=" + clientId;
            String sql = "UPDATE " + TABLE_NAME + str;
            int code = statement.executeUpdate(sql);
            if (code == 1) {
                success.code = 1;
                success.msg = "添加信息成功";
            } else {
                success.code = 0;
                success.msg = "updateData添加信息异常码:" + code;
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            success.msg = "丰沛:updateData添加客户失败";
            success.code = 0;
        }
        return success;
    }

    /**
     * 获取客户列表
     */
    @PostMapping("/userLogin")
    public BaseDateUser userLogin(String name, String password) {
        // 这里可以添加业务逻辑，例如保存用户到数据库
        BaseDateUser success = new BaseDateUser();
        success.code = 1;
        success.msg = "登录成功";
        String str = "123789FP" + EncryptUtils.salt;
        String token;
        try {
            byte[] bytes = EncryptUtils.StringToUTF8(str);
            token = EncryptUtils.sha256(bytes);
        } catch (Exception e) {
            success.code = 0;
            success.msg = "登录失败，获取token异常";
            return success;
        }
        if (!token.equals(password) || !name.equals("admin")) {
            success.code = 0;
            success.msg = "登录失败，密码或账号不正确" + "name:" + name + "password:" + password;
            return success;
        }
        User user = new User();
        user.name = name;
        user.token = token;
        success.setData(user);
        return success;
    }

}
