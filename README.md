学習した内容のアウトプットとして、社内での運用を想定した
商品受注管理用のwebアプリを作成しました。

# 使用技術
- Java
- Spring Boot
- MyBatis
- Thymeleaf
- MySql
- JavaScript

# 機能一覧
- ログイン機能
- アカウント
  作成・管理
  ロール機能
- 顧客・商品・案件情報（案件、受注、見積、問い合わせ）
　- 登録・編集
  - データ検索
  - 各リストのCSV出力

# 概要
顧客情報から案件情報を登録し、それに紐づく受注情報、見積情報、問い合わせ情報を登録、編集。  
受注情報や見積情報に登録する商品の編集や追加等も行えます。  
管理者アカウントでは、アプリを利用するアカウントの作成、編集も可能。  
管理者アカウントではなくとも、ログイン中のアカウント自身の編集機能もあります。

| ログイン画面 |　ログイン画面 |
| ---- | ---- |
| ![ログイン](https://github.com/user-attachments/assets/ff6dd574-f291-4a75-9751-18de2434816a) | ![案件一覧](https://github.com/user-attachments/assets/c4161873-ee1b-406b-84d5-ba5e2e3b6ae0) |
| ログインIDとパスワードでの認証機能を実装しました。| 案件の一覧画面です。表示件数の切り替えや、キーワード検索を行えます。 |

# 工夫した点 大変だった点
受注、見積、問い合わせの3つの機能を矛盾なく作るかという点です。

# ER図
![caseMng](https://github.com/user-attachments/assets/30816712-f4b6-4b10-8960-be93676c1d97)
