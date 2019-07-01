package com.zhejiangshegndian.csw.tool;

/**
 * Created by LeiQ on 2017/4/10.
 */

public class Constants {

    /** 微信APPID */
//    public static final String APP_ID_WX = "wx7202b6a02f0270b4"; // 橙袋
//    public static final String APP_ID_WX = "wxa4e24d97c901823a"; // 城市网Debug
    public static final String APP_ID_WX = "wxe424f029a98d915b"; // 城市网小蜜

    /** 七牛路径 */
//    public static String IMAGE = "http://oigx51fc5.bkt.clouddn.com/"; // 测试
    public static String IMAGE = "http://oofr2abh1.bkt.clouddn.com/"; // 正式
    // 无损压缩
    public static String COMPRESS = "?imageslim";

    /**
     * 服务器访问路径
     */
    // 测试环境
//    public static String URL = "http://139.196.32.206:";
//    public static String PORT = "5401/";
//    public static String API = "forward-service/api";
//    public static String SHARE = "http://test.csw.yiwocao.com/#/post/";
//    public static String ACTIVE = "http://test.huo.yiwocao.com/?comp=";

    // 研发环境
//    public static String URL = "http://106.15.103.7:";
//    public static String PORT = "5401/";
//    public static String API = "forward-service/api";
//    public static String SHARE = "http://csw.yiwocao.com/#/post/";
//    public static String ACTIVE = "http://huo.yiwocao.com?comp=";

    // 正式环境
    public static String URL = "http://114.55.179.135:";
    public static String PORT = "5401/";
    public static String API = "forward-service/api";
    public static String SHARE = "http://csw.wanyiwoo.com/#/post/";
    public static String ACTIVE = "http://huo.wanyiwoo.com?comp=";

    public static String SIGNOUT = "forward-service/user/logOut";


    /** 省市区查询公司 */
    public static final  String CODE_806012 = "806012";
    /** 查询所有公司 */
    public static final  String CODE_806017 = "806017";
    /** 发送短信验证码 */
    public static final  String CODE_805904 = "805904";
    /** 注册 */
    public static final  String CODE_805076 = "805076";
    /** 登陆 */
    public static final  String CODE_805043 = "805043";
    /** 第三方登陆 */
    public static final  String CODE_805151 = "805151";
    /** 找回密码 */
    public static final  String CODE_805048 = "805048";
    /** 每日签到 */
    public static final  String CODE_805100 = "805100";
    /** 查询轮播图 */
    public static final  String CODE_610107 = "610107";
    /** 发布帖子 */
    public static final  String CODE_610110 = "610110";
    /** 草稿发布帖子 */
    public static final  String CODE_610111 = "610111";
    /** 评论帖子 */
    public static final  String CODE_610112 = "610112";
    /** 举报帖子 */
    public static final  String CODE_610113 = "610113";
    /** 批量删除帖子/评论 */
    public static final  String CODE_610116 = "610116";
    /** 阅读帖子 */
    public static final  String CODE_610120 = "610120";
    /** 打赏帖子 */
    public static final  String CODE_610122 = "610122";
    /** 分页查询帖子 */
    public static final  String CODE_610130 = "610130";
    /** 列表查询帖子 */
    public static final  String CODE_610131 = "610131";
    /** 查询用户发帖数 */
    public static final  String CODE_610150 = "610150";
    /** 详情查询帖子 */
    public static final  String CODE_610132 = "610132";
    /** 分页查询帖子评论 */
    public static final  String CODE_610133 = "610133";
    /** 我收藏的帖子分页查询 */
    public static final  String CODE_610136 = "610136";
    /** 分页查询打赏 */
    public static final  String CODE_610142 = "610142";
    /** 分页查询帖子点赞 */
    public static final  String CODE_610141 = "610141";
    /** 提到我的评论分页查询 */
    public static final  String CODE_610143 = "610143";
    /** 提到我的帖子分页查询 */
    public static final  String CODE_610144 = "610144";
    /** 我发出的评论分页查询 */
    public static final  String CODE_610138 = "610138";
    /** 我收到的评论分页查询 */
    public static final  String CODE_610139 = "610139";
    /** 点赞帖子/取消点赞 */
    public static final  String CODE_610121 = "610121";
    /** 列表查询用户关系 */
    public static final  String CODE_805091 = "805091";
    /** 建立用户关系 */
    public static final  String CODE_805080 = "805080";
    /** 删除用户关系 */
    public static final  String CODE_805081 = "805081";
    /** 分页查询用户(无需token) */
    public static final  String CODE_805255 = "805255";
    /** 详情查询用户(无需token) */
    public static final  String CODE_805256 = "805256";
    /** 分页查询小模块 */
    public static final  String CODE_610045 = "610045";
    /** 详情查询小模块 */
    public static final  String CODE_610046 = "610046";
    /** 列表查询小模块 */
    public static final  String CODE_610047 = "610047";
    /** 查询论坛分类(列表查询大模块) */
    public static final  String CODE_610027 = "610027";
    /** 列表查询菜单 */
    public static final  String CODE_610087 = "610087";
    /** 列表查询子系统 */
    public static final  String CODE_610097 = "610097";
    /** 分页查询视频 */
    public static final  String CODE_610055 = "610055";
    /** 查询账户 */
    public static final  String CODE_802503 = "802503";
    /** 分页查询账户流水 */
    public static final  String CODE_802524 = "802524";
    /** 根据ckey查询系统参数 */
    public static final  String CODE_807717 = "807717";
    /** 修改登录名 */
    public static final  String CODE_805150 = "805150";
    /** 修改登录密码 */
    public static final  String CODE_805049 = "805049";
    /** 分页查询产品 */
    public static final  String CODE_808025 = "808025";
    /** 用户扩展信息修改 */
    public static final  String CODE_805156 = "805156";
    /** 修改手机号 */
    public static final  String CODE_805047 = "805047";
    /** 分页查询系统消息 */
    public static final  String CODE_804040 = "804040";
    /** 查询公司地址 */
    public static final  String CODE_806010 = "806010";
    /** 详情查询类别 */
    public static final  String CODE_808006 = "808006";
    /** 批量支付订单 */
    public static final  String CODE_808052 = "808052";
    /** 立即下单 */
    public static final  String CODE_808050 = "808050";
    /** 详情查询产品 */
    public static final  String CODE_808026 = "808026";
    /** 我的订单分页查询 */
    public static final  String CODE_808068 = "808068";
    /** 修改用户头像 */
    public static final  String CODE_805077 = "805077";
    /** 支付活动订单 */
    public static final  String CODE_660021 = "660021";

}

