```
TOKEN=$(curl -s localhost:8080/oauth/token \
  -d grant_type=password \
  -d username=0000000002 \
  -d password=aaaaa11111 | jq -r .access_token)
  

curl -s -H "Authorization: Bearer ${TOKEN}" localhost:8082/members/me | jq .


curl -s localhost:8082/members/me -H 'Content-Type: application/json' -d '
{
  "address": "東京都港区港南Ｘ－Ｘ－Ｘ",
  "birthday": "1990-01-01",
  "creditNo": "1111111111111111",
  "creditTerm": "01/20",
  "creditType": {
    "creditFirm": "VISA",
    "creditTypeCd": "VIS"
  },
  "gender": "M",
  "kanaFamilyName": "ヤマダ",
  "kanaGivenName": "タロウ",
  "kanjiFamilyName": "山田",
  "kanjiGivenName": "太郎",
  "mail": "yamada@example.com",
  "tel": "111-1111-1111",
  "zipCode": "1111111",
  "password": "aaaaa11111"
}
'

curl -s localhost:8082/members/me -H 'Content-Type: application/json' -H "Authorization: Bearer ${TOKEN}" -XPUT -d '
{
  "address": "東京都港区港南Ｘ－Ｘ－Ｘ",
  "birthday": "1990-01-01",
  "creditNo": "1111111111111111",
  "creditTerm": "01/20",
  "creditType": {
    "creditFirm": "VISA",
    "creditTypeCd": "VIS"
  },
  "gender": "M",
  "kanaFamilyName": "ヤマダ",
  "kanaGivenName": "タロウ",
  "kanjiFamilyName": "山田",
  "kanjiGivenName": "太郎",
  "mail": "yamada@example.com",
  "tel": "111-1111-1111",
  "zipCode": "1111111",
  "currentPassword": "aaaaa11111",
  "password": "aaaaa11111"
}
'

curl -s localhost:8082/members/me -H 'Content-Type: application/json' -H "Authorization: Bearer ${TOKEN}" -XPUT -d '
{
  "address": "東京都文京区湯島Ｘ－Ｘ－Ｘ",
  "birthday": "1990-01-01",
  "creditNo": "1111111111111111",
  "creditTerm": "01/20",
  "creditType": {
    "creditFirm": "VISA",
    "creditTypeCd": "VIS"
  },
  "gender": "M",
  "kanaFamilyName": "ヤマダ",
  "kanaGivenName": "タロウ",
  "kanjiFamilyName": "山田",
  "kanjiGivenName": "太郎",
  "mail": "yamada@example.com",
  "tel": "111-1111-1111",
  "zipCode": "1111111",
  "currentPassword": "aaaaa11111",
  "password": "aaaaa11111"
}
' | jq .
```

```
TOKEN=$(curl -s https://atrs.cfapps.io/oauth/token \
  -d grant_type=password \
  -d username=0000000002 \
  -d password=aaaaa11111 | jq -r .access_token)
  

curl -s -H "Authorization: Bearer ${TOKEN}" https://atrs-member.cfapps.io/members/me | jq .

curl -s https://atrs-member.cfapps.io/members/me -H 'Content-Type: application/json' -d '
{
  "address": "東京都港区港南Ｘ－Ｘ－Ｘ",
  "birthday": "1990-01-01",
  "creditNo": "1111111111111111",
  "creditTerm": "01/20",
  "creditType": {
    "creditFirm": "VISA",
    "creditTypeCd": "VIS"
  },
  "gender": "M",
  "kanaFamilyName": "ヤマダ",
  "kanaGivenName": "タロウ",
  "kanjiFamilyName": "山田",
  "kanjiGivenName": "太郎",
  "mail": "yamada@example.com",
  "tel": "111-1111-1111",
  "zipCode": "1111111",
  "password": "aaaaa11111"
}
'
```