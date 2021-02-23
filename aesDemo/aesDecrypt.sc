println("aesDecrypt.sc")

import $file.textUtils
import $file.cipherParams
import textUtils.textUtils._
import cipherParams.cipherParams._


val wd = os.pwd
val encryptedF = wd / "aesDemo" / "encrypted.txt"
val decryptedF = wd / "aesDemo" / "decrypted.txt"
val encryptedText: String = os.read(encryptedF)


val readB64s = encryptedText
val read: Array[Byte] = b642arb(readB64s)

def parseRead(read:Array[Byte]) = {
    val (ab, c) = read.splitAt(SaltSizeBytes + GCMIvSizeBytes)
    val (a, b) = ab.splitAt(SaltSizeBytes)
    (a,b,c)
}
val (salt, iv, ecText) = parseRead(read)


import java.security.spec.KeySpec
import java.security.{Key, SecureRandom}
import java.util.Base64

import javax.crypto.{Cipher, SecretKey, SecretKeyFactory}
import javax.crypto.spec.{GCMParameterSpec, PBEKeySpec, SecretKeySpec}

print("enter the passphrase> ")
val pp = scala.io.StdIn.readLine()

val skf: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA512")
val ks: KeySpec = new PBEKeySpec(pp.toCharArray, salt, PbkdIters, AesKeySizeBytes*8)
val sk: SecretKey = skf.generateSecret(ks)
val k: Key = new SecretKeySpec(sk.getEncoded, "AES")

val c = Cipher.getInstance(CipherAlgo)
val gcmPs = new GCMParameterSpec(GCMTagSizeBytes*8, iv)
c.init(Cipher.DECRYPT_MODE, k, gcmPs)
val dcText = c.doFinal(ecText).map(_.toChar)

os.write.over(decryptedF, dcText.mkString(""))
println("written decrypted text to decrypted.txt")
