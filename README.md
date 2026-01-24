# 💊 약대장 (PharmMaster)
내 손안의 스마트 복약 관리 및 약국 찾기 서비스

사용자의 복약 스케줄을 체계적으로 관리하고,  
Kakao Local API와 지도 API를 활용해 **주변 약국 정보를 손쉽게 찾을 수 있는**  
안드로이드 애플리케이션입니다.

## 프로젝트 정보
- **Type**: Personal Project
- **Period**: 2025.11 ~ 진행 중
- **Status**: 기능 구현 및 고도화 단계(Dev)

## 주요 기능
GitHub Issue를 통해 기능 단위로 관리하며 개발 중입니다.
- **회원 관리(Auth)**  
  Firebase Authentication 기반 이메일 회원가입 / 로그인 및 프로필 관리
- **약 정보 검색**  
  공공데이터 포털 API 연동을 통한 의약품 상세 정보 조회
- **약국 찾기(Map)**  
  Google Maps SDK + Kakao Local API를 활용한 내 주변 약국 위치 및 상세 정보 제공
- **1:1 상담**  
  Firebase Firestore 활용한 사용자–약사 간 게시글 기반 문의/답변 기능
- **복약 관리**  
  복약 스케줄 등록 및 알림 관리 기능

## 기술 스택
### Android Core
- **Language**: Kotlin
- **UI**: Jetpack Compose (Material3)
- **Architecture**: MVVM, Clean Architecture  
- **DI**: Hilt
- **Async**: Coroutines, Flow

### Network & Data
- **Network**: Retrofit2, OkHttp3
- **API**: Kakao Local API, 공공데이터 포털
- **Image Loading**: Coil

### Backend (Serverless)
- **Auth**: Firebase Authentication
- **DB**: Cloud Firestore
- **Storage**: Firebase Cloud Storage

### Map & Location
- **SDK**: Google Maps SDK for Android
- **Location**: FusedLocationProviderClient

## 시스템 아키텍처
유지보수성과 테스트 용이성을 고려하여  
**Clean Architecture 기반으로 계층을 분리**했습니다.

- **Presentation Layer**  
  ViewModel + Jetpack Compose를 활용한 UI 상태(State) 관리 및 사용자 이벤트 처리
- **Domain Layer**  
  UseCase와 Repository Interface를 통해 비즈니스 로직을 순수하게 유지
- **Data Layer**  
  Retrofit(API), Firestore(DB)를 통한 데이터 처리 및 Hilt 기반 의존성 주입

## 데이터 모델링
NoSQL 기반인 Firestore의 장점을 살리기 위해, **읽기(Read) 성능을 최적화하는 방향**으로 데이터 구조를 설계했습니다.

<p align="center">
  <img width="800" height="961" alt="mongoDB" src="https://github.com/user-attachments/assets/7307629b-dfb7-4f01-97c5-d43b16a91c41" />
</p>

### 설계 포인트
* **조회 성능 최적화 :** 약 정보(`Medication`) 내에 복약 스케줄(`schedules`)을 포함하여 N+1 쿼리 문제를 방지
* **데이터 일관성 :** 복약 기록(`IntakeRecords`)은 데이터 양이 많아질 수 있으므로 별도 컬렉션으로 분리하고 참조(Reference) 방식으로 설계

## 핵심 기술적 도전

### 1. 날짜/시간 데이터 타입 설계 최적화
- **고민**  
  LocalDateTime(Java8+) 사용 시 모듈 간 의존성 증가와 API 레벨 호환성(@RequiresApi) 이슈 발생
- **해결**  
  - Domain / Data Layer: 플랫폼 의존성을 제거하기 위해 Long(Epoch Milli) 타입으로 데이터 이동 및 정렬 처리  
  - Presentation Layer: UI 표시 시점에만 LocalDateTime으로 변환하는 Mapper 패턴 적용  
  → 모듈 간 결합도를 낮추고 테스트 및 유지보수성 향상

### 2. Firestore 기반 반응형 상담 게시판 구현
- **고민**  
  게시글 형태의 상담 서비스에서 문의 등록이나 답변 완료 시, 새로고침 없이 즉시 UI에 반영되는 사용자 경험 필요
- **해결**  
  Firestore SnapshotListener와 Kotlin callbackFlow를 결합하여  
  데이터 변경 사항을 실시간 스트림으로 구독하는 구조를 설계하고 게시글 목록을 반응형으로 동기화

### 3. 지도 API와 로컬 데이터의 효율적 결합
- **고민**  
  Google Maps SDK만으로는 국내 약국의 상세 정보 검색에 한계 존재
- **해결**  
  지도 렌더링은 Google Maps SDK를 사용하고,  
  약국 검색은 정확도가 높은 Kakao Local API를 활용하여 두 서비스를 결합하는 로직 설계


## 트러블 슈팅

### 1. Google Maps 초기화 크래시 (Lifecycle Race Condition)
- **문제**  
  앱 실행 직후 지도 화면 진입 시 `CameraUpdateFactory is not initialized` 에러와 함께 강제 종료 발생
- **원인 분석**  
  Jetpack Compose의 빠른 렌더링으로 인해 Google Maps SDK 초기화 이전에  
  `LaunchedEffect`에서 카메라 이동 명령이 실행되는 타이밍 이슈 확인
- **해결**  
  `Application` 클래스의 `onCreate()`에서 `MapsInitializer`를 선행 호출하여
  SDK 초기화 시점을 명확히 제어하고 지도 화면 진입 안정성 확보  
<!--  👉 블로그 포스팅: 지도 SDK 초기화 타이밍 이슈 해결 -->

### 2. BottomBar 네비게이션 스택 오염 문제
- **문제**  
  Home 탭에서 Full-Screen 화면 진입 후 다른 탭 이동
  → 복귀 시 HomeMain이 아닌 이전 Full-Screen 화면이 복원되는 현상 발생
- **원인 분석**  
  Navigation의 `saveState / restoreState` 동작 시
  탭 내부 스택뿐 아니라 상위 화면까지 상태로 저장되는 구조적 문제
- **해결**  
  Full-Screen 화면 이동 시 `popUpTo` 옵션을 명확히 설정하고
  탭 네비게이션과 독립적으로 관리되도록 네비게이션 그래프 분리

### 3. Firestore 실기기 동기화 지연 문제 (Offline Persistence)
- **문제**  
  에뮬레이터에서는 정상 동작하나, 실기기에서 데이터 업로드가 장시간 지연되는 현상 발생
- **원인 분석**  
  코드 문제가 아닌 네트워크 환경 문제로 인해 Firestore가 오프라인 모드로 동작하며  
  데이터가 로컬 캐시에만 저장되고 있음을 확인
- **해결**  
  Firestore의 오프라인 동작 원리를 파악하고  
  네트워크 연결 상태를 체크하는 로직을 추가하여 테스트 환경 개선


## 🚧 추후 계획
- **Local Caching**: Room Database 도입으로 오프라인 환경에서도 복약 정보 조회 가능하도록 개선

<!--
## 📸 실행 화면 (Screenshots)

| 로그인 / 회원가입 | 약국 지도 검색 | 약 정보 상세 | 1:1 상담 채팅 |
|------------------|---------------|--------------|---------------|
| <img src="" width="200" /> | <img src="" width="200" /> | <img src="" width="200" /> | <img src="" width="200" /> |

-->
