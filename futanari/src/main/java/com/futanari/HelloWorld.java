package com.futanari;

import com.futanari.util.AESUtil;
import com.futanari.util.WXBizDataCrypt;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class HelloWorld {

	public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		String appId = "wx4f4bc4dec97d474b";
		String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
		String encryptedData =
				"CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZMQmRzooG2xrDcvSnxIMXFufNstNGTyaGS9uT5geRa0W4oTOb1WT7fJlAC+oNPdbB+3hVbJSRgv+4lGOETKUQz6OYStslQ142dNCuabNPGBzlooOmB231qMM85d2/fV6ChevvXvQP8Hkue1poOFtnEtpyxVLW1zAo6/1Xx1COxFvrc2d7UL/lmHInNlxuacJXwu0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn/Hz7saL8xz+W//FRAUid1OksQaQx4CMs8LOddcQhULW4ucetDf96JcR3g0gfRK4PC7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns/8wR2SiRS7MNACwTyrGvt9ts8p12PKFdlqYTopNHR1Vf7XjfhQlVsAJdNiKdYmYVoKlaRv85IfVunYzO0IKXsyl7JCUjCpoG20f0a04COwfneQAGGwd5oa+T8yO5hzuyDb/XcxxmK01EpqOyuxINew==";
		String iv = "r7BXXKkLb8qrSNn05n0qiA==";

//		sessionKey = "ejza7rAul+nLybV2Q/FpPA==";
//		encryptedData =
//				"DBYSdpX41Mptb27Rbtj0LqcJm6tpeBwgX8HmUZfekdoyDmq7V7x7eXsfn4GMhBbodhpyQcoBtBCiMQgWAMOMKZJIsrWWWI5coyDDX1OpvlsmUuVHhKmcU12tp6X65ESqltVEaZzn6GSMTMRqfdFk+tnN1ZD86SLA0jCAIV66CKMl841UlF5cYuOWS70NNhQpeVUIF2ZhsWH5XDJnwdli7pLrO5Y8CjCzg+akVN+Hjo2SGRoF4OxrG3G3z9o7wvk7QkItFMHy4H4V2XtTDhLl2Xt7lNIXn5af6JQb4NSg0ginUHcUOoo1CeNdAnR7JvL6GJfPp9CVQQe623xrcrQfDvcTPSKQ5bdBMgB/WJDfiGHIyulJDcvsrp4mJHLbVYsLjSVg2nM6J1KMeQaxVSwJCGmZ0R5AOSon7PB4p250+fcpnCws+49YsH4bL0hud6W5FYd4N1iIzc6RdcA4hlZN7A==";
//		iv = "A/jon9ngM10MEf1xIg/lIg==";

//		sessionKey = new String(Base64Util.decode(sessionKey));
//		encryptedData = new String(Base64Util.decode(encryptedData));
//		iv = new String(Base64Util.decode(iv));

//		Cipher adecipher = Cipher.getInstance("AESUtil/CBC/PKCS7Padding");

		AESUtil aesUtil = new AESUtil();
		byte[] resultByte = aesUtil.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
		if(null != resultByte && resultByte.length > 0){
			String userInfo = new String(WXBizDataCrypt.decode(resultByte));
			System.out.println(userInfo);
		}

	}

}
