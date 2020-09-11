package xcxdecoding;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xcxdecoding.util.Decoding;

import java.util.Base64;

/**
 * 小程序授权手机号解密
 */
public class PhoneDecoding {

    public static void main(String[] args) {
        //小程序授权后获取
        getPhoneNumber("sessionKey","encryptedData","iv");
    }

    public static String getPhoneNumber(String sessionKey,String encryptedData,String iv){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] sessionKeyBy = decoder.decode(sessionKey);
        byte[] encryptedDataBy = decoder.decode(encryptedData);
        byte[] ivBy = decoder.decode(iv);
        byte[] dec = Decoding.decryptOfDiyIV(encryptedDataBy, sessionKeyBy,ivBy);
        JSONObject jsonObject = JSON.parseObject(new String(dec));
        String phoneNumber = null;
        if(jsonObject.containsKey("phoneNumber")){
            phoneNumber = (String) jsonObject.get("phoneNumber");
        }
        return phoneNumber;
    }

}
