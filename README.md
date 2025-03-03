学習した内容のアウトプットとして、社内での運用を想定した商品受注管理用のwebアプリを作成しました。  
顧客別に問い合わせ、見積、受注等の情報の管理を管理するものです。

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
管理者アカウント　id:admin pw:pass  
一般アカウント　id:user pw:test

# 使い方
- 流れ  
顧客情報を登録し、問い合わせ・見積・受注情報を登録していきます。見積・受注情報には商品を登録します。

- ログイン画面  
![ログイン](https://github.com/user-attachments/assets/d8b2ee94-224a-4f0c-97bf-2dec1988107d)  
ログインIDとパスワードでログインします。

- 案件一覧  
![案件一覧](https://github.com/user-attachments/assets/6fdedff8-dcb3-442e-ad7e-150896f28988)  
案件の一覧画面です。表示件数の切り替えや、キーワード検索を行えます。 同様に顧客一覧の画面もあります。

- 顧客詳細画面  
![顧客詳細](https://github.com/user-attachments/assets/23dbb0dd-e5e4-43eb-a02f-8123d2afcf4e)  
顧客の詳細画面です。顧客、案件、受注、見積、問い合わせに関しての情報を登録・編集・削除が可能です。  
1件の案件データについて、受注・見積・問い合わせのデータが紐づいており、上部の「表示案件」のIDを切り替えるとそれに紐づく受注・見積・問い合わせデータが表示されます。  
見積・問い合わせデータは１件の案件データにつき複数のデータが登録可能です。

- 案件編集画面
![案件編集](https://github.com/user-attachments/assets/2dbbcbbd-1c8e-4d9c-bdff-c7d84ad3dd04)  
案件編集画面です。顧客詳細画面から受注登録を行うと発送ステータスが表示され、編集可能になります。
発送ステータスを「発送済み」にするとその案件は「送付処理済」とされ、以後発送ステータスは「発送済み」か「キャンセル」しか選択できず、受注情報は編集不可となります。

- 受注編集画面  
![受注編集](https://github.com/user-attachments/assets/da72ea5a-4ece-4625-8259-a579efc1cf51)  
受注編集画面です。「商品追加ボタン」で商品の追加も可能です。削除は商品名を空白、又は在庫を0で保存すると反映されます。見積編集についても同様です。

- CSV出力
![csv出力](https://github.com/user-attachments/assets/ae6debe6-21f1-42e2-8958-81a73529e280)  
画面左上の「CSV」ボタンで、表示のリストをCSVファイルとしてエクスポートします。画面上部の「CSV出力」プルダウンメニューで、顧客・案件・受注・見積・問い合わせの各リストが出力可能です。

- 商品一覧  
![商品一覧](https://github.com/user-attachments/assets/a40dbf2c-c25b-42b7-acb2-330fda80e759)  
受注・見積情報にて使用する商品の一覧です。

- 商品編集  
 ![商品編集](https://github.com/user-attachments/assets/28d725b4-f52b-4beb-b3b4-675b552e97e9)  
商品の編集画面です。商品情報を編集可能です。商品の情報、受注・見積にて商品登録の可不可が設定が可能です。

- アカウント設定  
![アカウント設定](https://github.com/user-attachments/assets/b3608237-2709-412a-960b-86c81b48d132)  
ログイン中のアカウントの設定画面です。表示名・メールアドレス・パスワードが変更可能です。

- アカウント一覧  
![アカウント一覧](https://github.com/user-attachments/assets/711fe6b0-cf8f-4cba-a511-27b94ae5398b)  
管理者アカウントのみが使用できるアカウント管理機能です。

- アカウント編集  
![アカウント編集](https://github.com/user-attachments/assets/01a4e267-0486-4412-8cde-6c7cf0396d27)  
アカウントの管理画面です。アカウント情報の編集、アカウントの有効化・無効化が可能です。
