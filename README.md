#  頂き物管理 Webアプリケーション - Unit Test Update Edition
本プロジェクトは、以前公開した「Security Update Edition」をベースに、JUnit 5 / Mockito / AssertJ を導入し、単体テストを徹底実装したアップデート版です。
ロジックの正当性を担保するため、全サーブレットにおける網羅率（Coverage）100% を達成しました。

<img width="951" height="277" alt="スクリーンショット 2026-03-19 163423" src="https://github.com/user-attachments/assets/e106081e-8ca8-40ea-be0e-4efb8f1d6c74" />

## 🛠️ 開発の内容
単にテストを書くだけでなく、テスト容易性を高めるための構造的リファクタリングを実施しました。

- サーブレットの単体テスト完遂: MockedStatic を活用し、LogicFactory 等の静的メソッドを制御。DBや外部環境に依存しない、高速かつ安定したテストを実現。

- リファクタリングによる出口の集約: Early Return が多発し複雑化したロジックを整理。遷移先（Redirect/Forward）を変数管理し、出口を一つに絞ることで、カバレッジの死角を排除しました。

- 網羅率100%の達成: JaCoCoを用いて計測を行い、正常系・異常系・境界値（セッションの有無やトークン不一致等）の全ルートを検証済み。

- 可読性の高いテスト構成: @Nested を活用し、「どのメソッドの、どの状態をテストしているか」を構造的に整理。

## 💻 使用技術一覧
言語: Java SE 21

フレームワーク: Servlet / JSP / JSTL

テスト: JUnit 5 / Mockito / AssertJ / JaCoCo

DB: MySQL 8.4

インフラ: Docker / Docker Compose

開発環境: Eclipse 2025-09 / Apache Tomcat 10 / Maven

## 🚀 Dockerの起動手順
Dockerがインストールされた環境であれば、以下の手順ですぐに動作確認が可能です。

1.リポジトリをクローン
以下のコマンドを実行してください。

```Bash
git clone https://github.com/wakinoza/gift_history_servlet_db_app
cd gift_history_servlet_db_app
```

2.環境変数の準備
.env.example をコピーして .env を作成し、必要な値を設定してください。

3.コンテナの起動
以下のコマンドを実行してください。

```Bash
docker compose up -d --build
```

4.アプリの確認
ブラウザで http://localhost:8080/gift/index.jsp にアクセスしてください。

ユーザー名：yamada / パスワード：yamada_password でログイン可能です。

5.テストの実行（Maven）

```Bash
mvn test
```

## 🧠 開発で難しかった点

- 「入れ子構造のtry-with-resources」と「多すぎるreturn」による網羅率の停滞

当初、メソッド内に多数の return 文が存在し、条件分岐が複雑に絡み合っていたため、JaCoCoでの網羅率が100%に届かない事態に遭遇しました。特に try-with-resources が内部で生成する暗黙的な分岐（リソースのクローズ処理等）と、明示的な if 分岐が組み合わさることで、JUnitでの判定条件が複雑化しすぎたことが原因でした。

## 💡 得られた教訓
- 「テスト容易性（Testability）を意識した設計」の重要性
「テストが書きにくい、あるいは網羅できないのは、設計に改善の余地があるサインである」という事実を深く認識しました。
今回、サーブレットの出口を整理するリファクタリングを行った結果、テストコードが劇的にシンプルになっただけでなく、将来の仕様変更時にも修正箇所を最小限に抑えられる「変更に強いコード」へと進化しました。テストを意識して開発することは、結果として開発工程全体の削減と品質向上に直結することを学びました。

## 🔭 今後の展望

‐ Spring Framework への移行: 本アプリで培った Servlet/JSP の基礎、およびテストのノウハウを活かし、Springフレームワークを用いたDI/AOP開発へステップアップします。
