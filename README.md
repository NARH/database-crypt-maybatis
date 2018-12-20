# database-crypt-maybatis

springboot と mybatis の組み合わせで、
データベース(H2)の個人情報項目を暗号化する取り組みです。

H2 のデータベースファイルを暗号化するものとは別です。

暗号化は AES256 -> 排他的論理和で２重に暗号化し、
複合はその逆を行います。

## 起動方法

` com.github.narh.example001.mybatis.Application ` を起動します。

## 確認方法

 http://localhost:8080 でデータ入力後に
 http://localhost:8080/h2-console/ で暗号化されていることを確認します。

 ## 実現方法

 `mybatis`　の type handler を使って文字列を暗号化バイナリに変換しています。

