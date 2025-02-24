学習した内容のアウトプットとして、社内での運用を想定した
商品受注管理用のwebアプリを作成しました。

# 使用技術
- Java
- Spring Boot
- Spring Security
- MyBatis
- Thymeleaf
- DataTables
- MySql
  - H2 Database(動作確認用として実装)
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

# 動作確認
動作確認用としてH2データーベース実装とsqlファイルをresources直下に配置しています。  
spring bootプロジェクトとしてインポートしてご利用ください。
管理者アカウント
id:admin pw:pass
一般アカウント
id:user pw:test

# 使い方
| ログイン画面 |　一覧 |
| ---- | ---- |
| ![ログイン](https://github.com/user-attachments/assets/ff6dd574-f291-4a75-9751-18de2434816a) | ![案件一覧](https://github.com/user-attachments/assets/c4161873-ee1b-406b-84d5-ba5e2e3b6ae0) |
| ログインIDとパスワードでログインします。| 案件の一覧画面です。表示件数の切り替えや、キーワード検索を行えます。 同様に顧客、商品、アカウント（管理者のみ）の一覧画面もあります。|
| 顧客詳細画面 | 編集画面 |
| ![顧客詳細](https://github.com/user-attachments/assets/77a7e366-afe0-4c7d-a443-2da5b1c1997c) | ![受注編集](https://github.com/user-attachments/assets/b1e6017c-cd1c-4287-96a4-4f568c6d4a15) |
| 顧客の詳細画面です。紐づく案件、受注、見積、問い合わせ情報を確認、編集できます。 | 案件編集画面です。商品の追加も可能です。削除は在庫を0以下で保存すると反映されます。 |
| ![csv出力](https://github.com/user-attachments/assets/4e19acd3-5b4a-4c00-b6ee-0c3b64b7502a) 
| 各リストをcsv出力できます。
