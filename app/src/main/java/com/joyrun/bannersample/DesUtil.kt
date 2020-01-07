package com.joyrun.bannersample

import android.util.Base64
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

/**
 * author: wenjie
 * date: 2020-01-02 10:40
 * descption:
 */
class DesUtil {

    companion object {
        /**
         * 偏移变量，固定占8位字节
         */
        private const val IV_PARAMETER = "12345678"
        /**
         * 密钥算法
         */
        private const val ALGORITHM = "DES"
        /**
         * 加密/解密算法-工作模式-填充模式
         */
        private const val CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding"
        /**
         * 默认编码
         */
        private const val CHARSET = "utf-8"

        /**
         * 生成key
         *
         * @param password 密码
         * @return key
         */
        @Throws(Exception::class)
        private fun generateKey(password: String): Key? {
            val dks =
                DESKeySpec(password.toByteArray(charset(CHARSET)))
            val keyFactory =
                SecretKeyFactory.getInstance(ALGORITHM)
            return keyFactory.generateSecret(dks)
        }

        /**
         * DES加密字符串
         *
         * @param password 加密密码，长度不能够小于8位
         * @param data 待加密字符串
         * @return 加密后内容
         */
        @JvmStatic
        fun encrypt(password: String?, data: String?): String? {
            if (password == null || password.length < 8) {
                throw RuntimeException("加密失败，key不能小于8位")
            }
            return if (data == null) null else try {
                val secretKey = generateKey(password)
                val cipher =
                    Cipher.getInstance(CIPHER_ALGORITHM)
                val iv =
                    IvParameterSpec(IV_PARAMETER.toByteArray(charset(CHARSET)))
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
                val bytes = cipher.doFinal(data.toByteArray(charset(CHARSET)))
                //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
                //Android平台可以使用android.util.Base64
                String(Base64.encode(bytes, Base64.DEFAULT))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                data
            }
        }

        /**
         * DES解密字符串
         *
         * @param password 解密密码，长度不能够小于8位
         * @param data 待解密字符串
         * @return 解密后内容
         */

        fun decrypt(password: String?, data: String?): String? {
            if (password == null || password.length < 8) {
                throw java.lang.RuntimeException("加密失败，key不能小于8位")
            }
            return if (data == null) null else try {
                val secretKey = generateKey(password)
                val cipher =
                    Cipher.getInstance(CIPHER_ALGORITHM)
                val iv =
                    IvParameterSpec(IV_PARAMETER.toByteArray(charset(CHARSET)))
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
                String(
                    cipher.doFinal(
                        Base64.decode(
                            data.toByteArray(charset(CHARSET)),
                            Base64.DEFAULT
                        )
                    ), charset(CHARSET)
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                data
            }
        }
    }
}