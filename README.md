# Playmon - 실시간 롤 플레이 알림
## 요구 사항
- 검색 기능
  - 사용자는 이름/태그를 통해 원하는 플레이어를 검색할 수 있다. (Ex. `HideOnBush#KR1`)
  - 검색 결과를 클릭해 플레이어 페이지로 이동할 수 있다.
- 구독 기능
  - 검색 시 플레이어의 '플레이어 아이콘', '라이엇 아이디(이름/태그)'가 보여야 한다.
  - 유저는 구독 버튼을 눌러 해당 플레이어를 구독할 수 있다.
  - 유저는 '내 구독 목록'에서 구독한 플레이어 목록을 볼 수 있다.
  - 유저는 구독을 해제할 수 있다.
- 알림 기능
  - 구독한 플레이어가 게임을 시작하면 유저는 푸시 알림을 받아야 한다.

## 구현 방법 및 주의 사항
- `/lol/spectator/v5/active-games/by-summoner/{puuid}`를 통해 게임이 진행중이라는
  것을 확인했을 때, 마지막으로 플레이한 gameId와 현재 gameId를 비교해 새 게임이 시작된 것인지 기존
  게임이 진행 중인지 구분해야 한다.

| 상황            | 새 게임 시작 여부 | 게임 정보 업데이트 여부 |
|---------------|------------|---------------|
| 게임 없음 -> 새 게임 | O          | O             |
| 게임 A -> 게임 B  | O          | O             |
| 게임 A -> 게임 A  | X          | X             |
| 게임 A -> 게임 없음 | X          | X             |

- 1명 이상이 구독하고 있는 플레이어에 대해 돌아가면서 새 게임을 시작했는지 확인한다.
  만약 새 게임을 시작했다면 구독자에게 푸시 알림을 전송한다.
- 플레이어 검색 시 작동
  1. 유저가 gameName + tagLine으로 플레이어를 검색한다.
  2. gameName + tagLine을 통해 puuid을 얻는다.
     1. gameName + tagLine이 DB에 있으면 riot api 요청 없이 얻는다.
     2. gameName + tagLine이 DB에 없으면 riot api 요청을 통해 얻는다.
  3. riot api를 통해 puuid로 profileIconId, revisionDate, summonerLevel을 얻는다.
  4. DB에 얻은 정보를 저장/업데이트한다.
  5. 유저에게 gameName, tagLine, profileIconId, summonerLevel을 반환한다.

## 사용하는 riot-api 스펙
- `/lol/summoner/v4/summoners/by-puuid/{puuid}`
  - puuid를 통해 gameName, tagLine이 변경되었는지 확인하고 profileIconId를 얻는다
  - input: puuid
  - output: puuid, profileIconId, revisionDate, summonerLevel
- `/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}`
  - 라이엇 아이디(이름/태그)를 통해 puuid를 얻을 수 있다
  - input: gameName, tagLine
  - output: puuid, gameName, tagLine
- `/lol/spectator/v5/active-games/by-summoner/{puuid}`
  - puuid를 통해 현재 진행 중인 게임 정보를 얻을 수 있다
  - input: puuid
  - output: gameId, gameStartTime 등 또는 404 error