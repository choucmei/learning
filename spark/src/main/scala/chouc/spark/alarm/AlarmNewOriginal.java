package chouc.spark.alarm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author chouc
 * @version V1.0
 * @Title: AlarmNewOriginal
 * @Package chouc.spark.alarm
 * @Description:
 * @date 2021/2/24
 */
@Slf4j
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmNewOriginal implements Serializable {

    private static final long serialVersionUID = 1555211545083230563L;

    private long id;
    /**
     * '主键'
     */
    @NotNull(message = "keyid不能为空")
    private String keyid;
    /**
     * '证件号码' 身份证 手机号或者车牌
     */
    @NotNull(message = "key不能为空")
    private String key;
    /**
     * '姓名'
     */
    private String xm;
    /**
     * '性别'
     */
    private String xb;
    /**
     * '民族'
     */
    private String mz;
    /**
     * '户籍地'
     */
    private String hjd;
    /**
     * '活动类别代码'
     */
    private String hdlbdm;
    /**
     * '活动类别名称'
     */
    private String hdlbmc;
    /**
     * '活动发生地区划代码（进）'
     */
    @JsonProperty("hdfsdqhdm_in")
    private String hdfsdqhdmIn;
    /**
     * '活动发生地区划名称（进）'
     */
    @JsonProperty("hdfsdqhmc_in")
    private String hdfsdqhmcIn;
    /**
     * '活动发生地区划代码（出）'
     */
    @JsonProperty("hdfsdqhdm_out")
    private String hdfsdqhdmOut;
    /**
     * '活动发生地区划名称（出）'
     */
    @JsonProperty("hdfsdqhmc_out")
    private String hdfsdqhmcOut;
    /**
     * '活动发生地代码（进）'
     */
    @JsonProperty("hdfsddm_in")
    private String hdfsddmIn;
    /**
     * '活动发生地代码（出）'
     */
    @JsonProperty("hdfsddm_out")
    private String hdfsddmOut;
    /**
     * '活动发生地名称（进）'
     */
    @JsonProperty("hdfsdmc_in")
    private String hdfsdmcIn;
    /**
     * '活动发生地名称（出）'
     */
    @JsonProperty("hdfsdmc_out")
    private String hdfsdmcOut;
    /**
     * '活动发生地详址（进）'
     */
    @JsonProperty("hdfsdxz_in")
    private String hdfsdxzIn;
    /**
     * '活动发生地详址（出）'
     */
    @JsonProperty("hdfsdxz_out")
    private String hdfsdxzOut;
    /**
     * '活动备注信息'
     */
    private String hdbzxx;
    /**
     * '经纬度（进）'
     */
    @JsonProperty("jwd_in")
    private String jwdIn;
    /**
     * '经纬度（出）'
     */
    @JsonProperty("jwd_out")
    private String jwdOut;
    /**
     * '活动发生时间'
     */
    private Date hdfssj;
    /**
     * '活动结束时间'
     */
    private Date hdjssj;
    /**
     * 经纬度
     */
    private String jwd;
    /**
     * 经度_out
     */
    @JsonProperty("jd_out")
    private Double jdOut;
    /**
     * 纬度_out
     */
    @JsonProperty("wd_out")
    private Double wdOut;
    /**
     * 照片视频
     */
    private String zpsp;
    /**
     * 派出所代码
     */
    private String pcsdm;
    /**
     * 派出所
     */
    private String pcs;
    /**
     *
     */
    private String mbhh;
    /**
     * 备注
     */
    private String bz;
    /**
     * 模型名称
     */
    private String mxmc;
    /**
     * 目标分类
     */
    private String mbfl;
    /**
     * 关注类别
     */
    private String gzlb;
    /**
     *
     */
    private String userid;
    /**
     * 布控级别
     */
    private String bkjb;
    /**
     * 目标类别
     */
    private String mblb;
    /**
     *
     */
    private String yjxw;
    /**
     * 布控类型
     */
    private String bklx;
    /**
     * 布控id
     */
    private String bkid;
    /**
     * 经度_in
     */
    @JsonProperty("jd_in")
    private Double jdIn;
    /**
     * 纬度_in
     */
    @JsonProperty("wd_in")
    private Double wdIn;
    private Long alarmId;
}
