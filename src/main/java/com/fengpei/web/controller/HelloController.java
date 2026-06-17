package com.fengpei.web.controller;

import com.fengpei.web.WebClient.ExternalService;
import com.fengpei.web.entiry.*;
import com.fengpei.web.tool.BusinessTool;
import com.fengpei.web.tool.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.sql.DataSource;
import java.sql.*;
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
    public final static long REPETITION = -100215401;//数据库某些字段重复
    private final DataDao dataDao = new DataDao();
    public final BusinessTool businessTool = new BusinessTool();

    private final ExternalService externalService;

    public HelloController(ExternalService externalService) {
        this.externalService = externalService;
    }


    @GetMapping("/hello")
    public String hello() {
        return "helloWord";
    }

    @GetMapping("/resultData-tes")
    public BaseDataResult resultData(@RequestParam(value = "test", required = false) String test) {
        BaseDataResult success = new BaseDataResult();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
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
            closeResource(statement, connection);
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
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
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
            closeResource(statement, connection);
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
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
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
            closeResource(statement, connection);
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
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            int limitNum = 20;
            String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC " + " LIMIT " + limitNum + " OFFSET " + (page - 1) * limitNum;
            resultSet = statement.executeQuery(sql);
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
        } catch (SQLException e) {
            success.setCode(0);
            success.setMsg("丰沛:getUserList数据库操作失败");
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ignored) {
                }
            }
            closeResource(statement, connection);
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
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            int limitNum = 20;
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE status != 0 " + " ORDER BY submitTime DESC " + " LIMIT " + limitNum + " OFFSET " + (page - 1) * limitNum;
            resultSet = statement.executeQuery(sql);
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
        } catch (SQLException e) {
            success.setCode(0);
            success.setMsg("丰沛:getUserList数据库操作失败");
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ignored) {
                }
            }
            closeResource(statement, connection);
        }
        success.setData(clientList);
        return success;
    }

    @PostMapping("/deleteFirstListClients")
    public BaseData deleteFirstListClients(String Password) {
        BaseData baseData = new BaseData();
        if (Password == null || !Password.equals("987321FP")) {
            baseData.code = 1;
            baseData.msg = "密码错误";
            return baseData;
        }
        Connection connection = null;
        PreparedStatement ps = null;
        Statement countStmt = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            //删除最早100条（按id排序）
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE id IN (" + " SELECT id FROM (" + " SELECT id FROM " + TABLE_NAME + " ORDER BY id ASC LIMIT 10" + " ) t" + " )";
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
            // 查询剩余总数
            String countSql = "SELECT COUNT(*) FROM " + TABLE_NAME;
            countStmt = connection.createStatement();
            rs = countStmt.executeQuery(countSql);
            int remainCount = 0;
            if (rs.next()) {
                remainCount = rs.getInt(1);
            }
            baseData.code = 1;
            baseData.msg = "删除完成，当前剩余数据：" + remainCount + " 条";
        } catch (SQLException e) {
            baseData.code = 0;
            baseData.msg = "删除失败: " + e.getMessage();
        } finally {
            try {
                if (rs != null) rs.close();
                if (countStmt != null) countStmt.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) {
            }
        }
        return baseData;
    }

    /**
     * 删除客户
     */
    @PostMapping("/deleteClient")
    public BaseData deleteClient(Integer clientId) {
        BaseData baseData = new BaseData();
        if (clientId == null) {
            baseData.code = 1;
            baseData.msg = NULL_PARAMETER;
            return baseData;
        }
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                baseData.code = 1;
                baseData.msg = "删除成功";
            } else {
                baseData.code = 0;
                baseData.msg = "未找到数据，删除失败";
            }
        } catch (SQLException e) {
            baseData.code = 0;
            baseData.msg = "删除失败: " + e.getMessage();
        }
        return baseData;
    }

    /**
     * 删除客户
     */
    @PostMapping("/deleteClientByIdentityCardAndType")
    public BaseData deleteClientByIdentityCardAndType(String identityCard, String type) {
        BaseData baseData = new BaseData();
        if (identityCard == null || type == null) {
            baseData.code = 1;
            baseData.msg = NULL_PARAMETER;
            return baseData;
        }
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE identityCard = ? AND type = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, identityCard);
            ps.setString(2, type);
            int code = ps.executeUpdate();
            if (code > 0) {
                baseData.code = 1;
                baseData.msg = "删除成功";
            } else {
                baseData.code = 0;
                baseData.msg = "未找到数据，删除失败";
            }
        } catch (SQLException e) {
            baseData.msg = "删除数据失败: " + e.getMessage();
            baseData.code = 0;
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) {
            }
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
        String selectSql = "SELECT status FROM " + TABLE_NAME + " WHERE id = ?";
        String updateSql = "UPDATE " + TABLE_NAME + " SET status = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement selectPs = connection.prepareStatement(selectSql); PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
            // 1. 查询当前状态
            selectPs.setInt(1, clientId);
            try (ResultSet rs = selectPs.executeQuery()) {
                if (rs.next()) {
                    int currentStatus = rs.getInt("status");

                    if (currentStatus == 0) {
                        baseData.code = 1;
                        baseData.msg = "该用户还没有补交资料，你不能更改";
                        return baseData;
                    } else if (currentStatus == 2) {
                        baseData.code = 1;
                        baseData.msg = "该用户已经通过审核了，你不能更改";
                        return baseData;
                    } else if (currentStatus == 3) {
                        baseData.code = 1;
                        baseData.msg = "该用户已经被拒绝了，你不能更改";
                        return baseData;
                    }
                } else {
                    baseData.code = 0;
                    baseData.msg = "用户不存在";
                    return baseData;
                }
            }
            // 2. 更新状态
            updatePs.setInt(1, status);
            updatePs.setInt(2, clientId);
            int rows = updatePs.executeUpdate();
            if (rows == 1) {
                baseData.code = 1;
                baseData.msg = "修改成功";
            } else {
                baseData.code = 0;
                baseData.msg = "修改失败";
            }
        } catch (Exception e) {
            baseData.code = 0;
            baseData.msg = "修改客户失败: " + e.getMessage();
        }
        return baseData;
    }

    /**
     * 更改客户信息(通过/再次补充)
     */
    @PostMapping("/modifyRefuseClientData")
    public BaseData modifyRefuseClientData(Integer clientId, String refuseReasonOne) {
        BaseData baseData = new BaseData();
        if (clientId == null || refuseReasonOne == null || refuseReasonOne.isEmpty()) {
            baseData.code = 1;
            baseData.msg = NULL_PARAMETER;
            return baseData;
        }
        String selectSql = "SELECT status FROM " + TABLE_NAME + " WHERE id = ?";
        String updateSql = "UPDATE " + TABLE_NAME + " SET status = ?, refuseReasonOne = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement selectPs = connection.prepareStatement(selectSql); PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
            // 1. 查询当前状态
            selectPs.setInt(1, clientId);
            try (ResultSet rs = selectPs.executeQuery()) {
                if (rs.next()) {
                    int currentStatus = rs.getInt("status");

                    if (currentStatus == 0) {
                        baseData.code = 1;
                        baseData.msg = "该用户还没有补交资料，你不能更改";
                        return baseData;
                    } else if (currentStatus == 2) {
                        baseData.code = 1;
                        baseData.msg = "该用户已经通过审核了，你不能更改";
                        return baseData;
                    } else if (currentStatus == 3) {
                        baseData.code = 1;
                        baseData.msg = "该用户已经被拒绝了，你不能更改";
                        return baseData;
                    }
                } else {
                    baseData.code = 0;
                    baseData.msg = "用户不存在";
                    return baseData;
                }
            }
            // 2. 执行更新
            updatePs.setInt(1, 3); // 拒绝状态
            updatePs.setString(2, refuseReasonOne);
            updatePs.setInt(3, clientId);
            int rows = updatePs.executeUpdate();
            if (rows == 1) {
                baseData.code = 1;
                baseData.msg = "修改成功";
            } else {
                baseData.code = 0;
                baseData.msg = "修改失败";
            }
        } catch (Exception e) {
            baseData.code = 0;
            baseData.msg = "拒绝客户失败: " + e.getMessage();
        }
        return baseData;
    }

    public void closeResource(Statement statement, Connection connection) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ignored) {

        }
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
        String applicationNumber = businessTool.formalTool.getApplicationNumber(type);
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
            client.applicationNumber = applicationNumber;
            clientId = dataDao.addClientData(client, dataSource);
            if (clientId < 1) {
                if (clientId == REPETITION) {
                    success.code = 0;
                    success.msg = "该用户已申请过该产品了，请勿重复申请。";
                    return success;
                } else {
                    success.code = 0;
                    success.msg = "input失败";
                    return success;
                }
            }
        }
        success.code = 1;
        success.msg = "获取额度成功";
        success.setData(businessTool.setSelectDataContent(result, type, bankId, identityCard, clientId, applicationNumber));
        return success;
    }

    /**
     * 添加数据
     */
    @PostMapping("/getCalculateDate")
    public BaseDataCalculating getCalculateDate(Integer clientId) {
        BaseDataCalculating success = new BaseDataCalculating();
        if (clientId == null) {
            success.code = 1;
            success.msg = NULL_PARAMETER;
            return success;
        }
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + clientId;
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                Client client = businessTool.setClientData(resultSet);
                success.setData(businessTool.setSelectDataContent(client.assessMoney, client.type, client.bankId, client.identityCard, client.id, client.applicationNumber));
                success.code = 1;
                success.msg = "请求成功";
            } else {
                success.code = 0;
                success.msg = "没有数据";
            }
        } catch (SQLException e) {
            success.code = 0;
            success.msg = "丰沛:getCalculateDate数据库操作失败: " + e.getMessage();
        } catch (Exception e) {
            success.code = 0;
            success.msg = "丰沛:getCalculateDate解析数据异常: " + e.getMessage();
        }
        return success;
    }

    @PostMapping("/updateData")
    public BaseData updateData(int clientId, String educationBackground, String maritalStatus, String debt, String repayMonths, String presentAddress, String detailAddress, String livingModel, int livingSpend, String childrenNumber, String relativeOneName, String relativeOneBetween, String relativeOnePhone, String relativeTwoName, String relativeTwoBetween, String relativeTwoPhone, String colleagueOneName, String colleagueOnePhone, String colleagueTwoName, String colleagueTwoPhone, String companyname, String companytype, String companysector, String companyposition, String companytime, String leaderName, String companyScale, String monthSalary, String acquairSalaryType, String acquairSalaryDate, String companyAdress, String companyPhoneNumber, String commuteTime, String remark) {
        BaseData success = new BaseData();
        String currentTime = businessTool.formalTool.getCurrentTime();
        Connection connection = null;
        Statement statement = null;
        String str = " SET" + " educationBackground = " + "'" + educationBackground + "'" + ", maritalStatus = " + "'" + maritalStatus + "'" + ", debt = " + "'" + debt + "'" + ", repayMonths = " + "'" + repayMonths + "'" + ", presentAddress = " + "'" + presentAddress + "'" + ", detailAddress = " + "'" + detailAddress + "'" + ", livingModel = " + "'" + livingModel + "'" + ", livingSpend = " + livingSpend + ", childrenNumber = " + "'" + childrenNumber + "'" + ", relativeOneName = " + "'" + relativeOneName + "'" + ", relativeOneBetween = " + "'" + relativeOneBetween + "'" + ", relativeOnePhone = " + "'" + relativeOnePhone + "'" + ", relativeTwoName = " + "'" + relativeTwoName + "'" + ", relativeTwoBetween = " + "'" + relativeTwoBetween + "'" + ", relativeTwoPhone = " + "'" + relativeTwoPhone + "'" + ", colleagueOneName = " + "'" + colleagueOneName + "'" + ", colleagueOnePhone = " + "'" + colleagueOnePhone + "'" + ", colleagueTwoName = " + "'" + colleagueTwoName + "'" + ", colleagueTwoPhone = " + "'" + colleagueTwoPhone + "'" + ", companyname = " + "'" + companyname + "'" + ", companytype = " + "'" + companytype + "'" + ", companysector = " + "'" + companysector + "'" + ", companyposition = " + "'" + companyposition + "'" + ", companytime = " + "'" + companytime + "'" + ", leaderName = " + "'" + leaderName + "'" + ", companyScale = " + "'" + companyScale + "'" + ", monthSalary = " + "'" + monthSalary + "'" + ", acquairSalaryType = " + "'" + acquairSalaryType + "'" + ", acquairSalaryDate = " + "'" + acquairSalaryDate + "'" + ", companyAdress = " + "'" + companyAdress + "'" + ", companyPhoneNumber = " + "'" + companyPhoneNumber + "'" + ", commuteTime = " + "'" + commuteTime + "'" + ", remark = " + "'" + remark + "'" + ", submitTime = " + "'" + currentTime + "'" + ", status = " + 1 + " WHERE id=" + clientId;
        String sql = "UPDATE " + TABLE_NAME + str;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            int code = statement.executeUpdate(sql);
            if (code == 1) {
                success.code = 1;
                success.msg = "添加信息成功";
            } else {
                success.code = 0;
                success.msg = "updateData添加信息异常码:" + code;
            }
        } catch (SQLException e) {
            success.msg = "丰沛:updateData添加客户失败";
            success.code = 0;
        } finally {
            closeResource(statement, connection);
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


    @PostMapping("/sendMsg")
    public Mono<BaseData> sendMsg(Integer clientId, String content, String phone) {
        return externalService.postData(phone, content).flatMap(response -> {
            BaseData result = new BaseData();
            if (!"Success".equals(response.returnstatus)) {
                result.code = 2;
                result.msg = "发送失败: " + response.message;
                return Mono.just(result);
            }
            return Mono.fromCallable(() -> {
                String sql = "UPDATE " + TABLE_NAME + " SET sendMsg = 1 WHERE id = " + clientId;
                try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
                    int code = statement.executeUpdate(sql);
                    result.code = 1;
                    if (code == 1) {
                        result.msg = "发送成功，更新数据库成功";
                    } else {
                        result.msg = "发送成功，但更新数据库失败";
                    }
                    return result;
                }
            }).subscribeOn(Schedulers.boundedElastic());
        });
    }
}