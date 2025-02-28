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
- ログイン画面  
![ログイン](https://github.com/user-attachments/assets/e514a5b0-b77c-43e8-a6bc-a6cc87034b2d)  
ログインIDとパスワードでログインします。
- 案件一覧  
![案件一覧](https://github.com/user-attachments/assets/d7fe3a83-7e56-4054-9419-33c2ff8b669a)  
案件の一覧画面です。表示件数の切り替えや、キーワード検索を行えます。 同様に顧客一覧の画面もあります。
- 顧客詳細画面  
![詳細画面](https://github.com/user-attachments/assets/9a226ede-fe10-4a77-976c-c2dea2804ce5)  
顧客の詳細画面です。顧客、案件、受注、見積、問い合わせに関しての情報を登録、編集、削除が可能です。  
1件の案件データについて、受注・見積・問い合わせのデータが紐づいており、上部の「表示案件」のIDを切り替えるとそれに紐づく受注・見積・問い合わせデータが表示されます。  
見積・問い合わせデータは１件の案件データにつき複数のデータが登録可能です。
- 編集画面  
![受注編集](https://github.com/user-attachments/assets/f1a2b053-60b7-4112-b74a-370b1527cbf6)  
編集画面です。例として受注情報の編集画面を出しています。「商品追加ボタン」で商品の追加も可能です。削除は在庫を0以下で保存すると反映されます。
- CSV出力  
![csv出力](https://github.com/user-attachments/assets/627e5ef2-1519-4b1e-87c7-b6f69ffe43a7)  
各リストをcsv出力できます。顧客・案件・受注・見積・問い合わせのそれぞれのデータが出力可能です。

- アカウント設定  
![アカウント設定](https://github.com/user-attachments/assets/dd5eb14e-5806-43a2-bac8-9f9b55f7f659)  
ログイン中のアカウントの設定画面です。表示名・メールアドレス・パスワードが変更可能です。
- アカウント一覧  
![アカウント一覧](https://github.com/user-attachments/assets/f49e4bb8-1f1c-415d-b7d7-f7635e09ce9f)  
管理者アカウントのみが使用できるアカウント管理機能です。
- アカウント編集  
![アカウント編集](https://github.com/user-attachments/assets/a50ac656-987f-482b-bbc0-ba0e29dbc0cf)  
アカウントの管理画面です。アカウント情報の編集、アカウントの有効化・無効化が可能です。
- 商品一覧  
![商品一覧](https://github.com/user-attachments/assets/a3e54968-4d50-4612-95cc-96f0b5394833)  
受注・見積情報にて使用する商品の一覧です。
- 商品編集  
![商品編集](https://github.com/user-attachments/assets/8609d622-342b-4b2b-99d8-15084c3e4a9c)  
商品の編集画面です。商品情報を編集可能です。商品の情報、受注・見積にて商品登録の可不可が設定が可能です。
