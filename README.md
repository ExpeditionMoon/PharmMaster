# 💊 약대장 (PharmMaster)
**스마트 복약 관리 & 약사 O2O 상담 플랫폼**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-4285F4?logo=googleplay&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/DI-Hilt-717e1f?logo=android&logoColor=white)](https://developer.android.com/training/dependency-injection/hilt-android)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?logo=firebase&logoColor=black)](https://firebase.google.com/)

> 약대장은 흩어진 복약 정보를 한 곳에서 관리하고, 내 주변의 약사와 1:1로 연결되는 O2O 기반 헬스케어 서비스입니다.

<br>

## 📋 프로젝트 개요
* **프로젝트명**: 약대장 (PharmMaster)
* **유형**: Personal Project (기획/디자인/개발)
* **기간**: 2026.01 ~ 진행 중
* **상태**: 기능 구현 및 고도화 진행 중 (Dev)

<br>

## 📱 Preview
| 약사 등록 | 1:1 상담 요청 | 복약 관리 |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/9a1bd22c-500d-441a-a9e8-fa41c60cfc11" width="240" /> | <img src="https://github.com/user-attachments/assets/2f30ae12-5149-4e7a-aa96-07dbcd903071" width="240" /> | <img src="https://github.com/user-attachments/assets/bb18072d-6d77-4b12-8c6d-15601076c72c" width="240" /> |
| 약사는 약국을 선택해 가입 | 사용자 → 약사에게 상담 요청 | 복약 시간·기간 설정 및 알림 등록 |

<br>

## ✨ 주요 기능
| 기능 | 설명 |
| :--- | :--- |
| **🧾 회원가입 & 약사 등록** | 사용자 유형(일반/약사)에 따른 분기 처리, <br> 약사는 Google Maps SDK와 Kakao Local API  기반 약국 선택 후 가입 |
| **💊 복약 관리 & 알림** | 약 종류·복용 기간·시간 설정, **묶음 알림(Grouped Notification)** 지원, 캘린더 연동 <br/> OCR 복약 등록: Google ML Kit 기반 텍스트 추출로 처방전 자동 인식 및 복약 정보 등록 연결 |
| **💬 1:1 약사 상담 (O2O)** | 이미지 첨부가 가능한 상담 글 작성, 공개/비공개 설정, 약사 지정 상담 요청 <br/>Firestore SnapshotListener + FCM을 활용한 상담 답변 실시간 푸시 및 UI 반영 |
| **👤 마이페이지** | 복약 기록 캘린더 확인, 상담/답변 내역 관리, 프로필 수정 |


> ### 상세 기능 소개
각 항목을 클릭하면 상세 실행 화면을 볼 수 있습니다.

<details>
<summary><b>🧾 1. 회원가입 (Authentication)</b></summary>
<br>

| 일반 회원가입 | 약사 회원가입 (약국 선택) |
| :---: | :---: |
| <div align="center"><video src="https://github.com/user-attachments/assets/6e41b7cf-5cc4-41fa-8a68-d10d33fddc9a" width="200" height="400" controls></video><br>기본 정보 입력으로 간편 가입</div> | <div align="center"><video src="https://github.com/user-attachments/assets/2ec37fcc-f54e-4f13-9482-180499488663" width="200" height="400" controls></video><br>지도에서 근무 약국 선택 후 가입</div> |
</details>

<details>
<summary><b>💊 2. 복약 관리 & OCR 복약 등록 (Medication)</b></summary>
<br>

| 복약 설정 | 복약 알람 | 복약 기록 (캘린더) |
| :---: | :---: | :---: |
| <div align="center"><video src="https://github.com/user-attachments/assets/31230f8e-45c6-446b-b91b-750c311d21a2" width="200" height="400" controls></video><br>복약 시간 및 기간 상세 설정</div> | <div align="center"><video src="https://github.com/user-attachments/assets/8bba3d69-f632-4e28-9adf-8100d8affc96" width="200" height="400" controls></video><br>정해진 시간에 복약 푸시 알림</div> | <div align="center"><video src="https://github.com/user-attachments/assets/07bfdef4-8f46-42c2-a7a9-4ccbc5c37fcd" width="200" height="400" controls></video><br>캘린더에서 복약 이력 확인</div> |

| 카메라 OCR | 이미지 OCR |
| :---: | :---: |
| <div align="center"><video src="https://github.com/user-attachments/assets/53821b41-dfe9-4ead-963f-0cce111a5f2d" width="200" height="400" controls></video><br>처방전 촬영 후 텍스트 자동 추출</div> | <div align="center"><video src="https://github.com/user-attachments/assets/00cdcca6-124c-433f-8fe2-edce1bc6048a" width="200" height="400" controls></video><br>갤러리 이미지에서 약 정보 인식</div> |
</details>

<details>
<summary><b>💬 3. 약사 상담 (Consultation)</b></summary>
<br>

| 상담글 작성 | 약사: 글 알림 & 답장 | 일반: 답장 알림 | 상담 게시판 | 
| :---: | :---: | :---: | :---: | 
| <div align="center"><video src="https://github.com/user-attachments/assets/4b16060d-b800-4991-b87e-74f736642fad" width="200" height="400" controls></video><br>사진 첨부 및 상담 내용 작성</div> | <div align="center"><video src="https://github.com/user-attachments/assets/3f42bad6-5271-4d6d-86d1-1f0904e339d0" width="200" height="400" controls></video><br>새 상담 알림 수신 및 답변 등록</div> | <div align="center"><video src="https://github.com/user-attachments/assets/c6c6aca8-50da-4dca-9590-ae0cf9832ca7" width="200" height="400" controls></video><br>약사 답변 등록 시 즉시 알림</div> | <div align="center"><video src="https://github.com/user-attachments/assets/bbfcbef5-c352-418f-a8c9-b11b99ca31ea" width="200" height="400" controls></video><br>실시간 동기화되는 상담 목록</div> |
</details>

<details>
<summary><b>👤 4. 내 정보 (My Page)</b></summary>
<br>

| 약사: 나의 답변 내역 | 일반: 나의 상담 내역 | 나의 복약 기록 | 닉네임 수정 & 로그아웃 |
| :---: | :---: | :---: | :---: |
| <div align="center"><video src="https://github.com/user-attachments/assets/b11c03c3-9a91-4039-b24a-d266f203ccb8" width="200" height="400" controls></video><br>내가 작성한 답변 모아보기</div> | <div align="center"><video src="https://github.com/user-attachments/assets/4c7f53db-67e1-4f91-9a3d-b5a0e2aaca04" width="200" height="400" controls></video><br>나의 상담 질문 및 상태 확인</div> | <div align="center"><video src="https://github.com/user-attachments/assets/1b5df495-40ba-4816-bf85-7d4c377f5a46" width="200" height="400" controls></video><br>월별 복약 달성 현황 확인</div> | <div align="center"><video src="https://github.com/user-attachments/assets/fc053727-2dfd-4edb-bab2-922ae3334a3d" width="200" height="400" controls></video><br>프로필 관리 및 계정 설정</div> |
</details>

<br>

## 🏗️ 시스템 아키텍처
Clean Architecture 기반 멀티 모듈 구조로 확장성과 유지보수성을 확보했습니다.
<p align="center">
  <img width="800" height=auto alt="image" src="https://github.com/user-attachments/assets/9f2cb70a-a699-450a-88fe-63cb493aec90" />
</p>

* **`:features`**: 기능별 화면 UI 및 ViewModel (Presentation Layer)
* **`:domain`**: 비즈니스 로직 및 UseCase (Pure Kotlin)
* **`:data`**: 데이터 소스 및 Repository 구현체 (Data Layer)
* **`:component-ui`**: 앱 전반에서 재사용되는 공통 UI 컴포넌트 (Design System)

<br>

## 🛠️ 기술 스택 (Tech Stack)

<p align="center">
  <img src="https://github.com/user-attachments/assets/cedba8af-eb83-4cea-87b0-4d7bffd203fc" alt="Tech Stack" width="800"/>
</p>
<details>
<summary><b>사용 라이브러리 및 버전 상세 (Click)</b></summary>
<br>

| 구분 | 기술 / 라이브러리 |
| :--- | :--- |
| **Architecture** | Clean Architecture, MVVM, Multi-Module |
| **Android** | Kotlin, Jetpack Compose, Hilt, Coroutines, Flow |
| **Network** | Retrofit2, OkHttp3, Moshi |
| **Database** | Firebase Firestore (NoSQL) |
| **Location & Map** | Google Maps SDK, Kakao Local API |
| **Etc** | Google ML Kit (OCR), Coil (Image Loading) |

</details>

<br>

## 📐 데이터 모델링
NoSQL 기반의 데이터 구조를 시각화한 초기 설계도입니다. <br> 실제 구현 시에는 **Firestore**를 도입하여 컬렉션/문서 구조로 최적화했습니다.

<p align="center">
  <img width="800" alt="Conceptual Data Model" src="https://github.com/user-attachments/assets/7307629b-dfb7-4f01-97c5-d43b16a91c41" />
  <br>
  <sub>초기 설계 단계의 데이터 구조도 (Conceptual Model)</sub>
</p>

### 설계 포인트
* **조회 성능 최적화 :** 약 정보(`Medication`) 내에 복약 스케줄(`schedules`)을 포함하여 N+1 쿼리 문제를 방지
* **데이터 일관성 :** 복약 기록(`IntakeRecords`)은 데이터 양이 많아질 수 있으므로 별도 컬렉션으로 분리하고 참조(Reference) 방식으로 설계

## 🚀 핵심 기술적 도전

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


## 🩺 트러블 슈팅

### 1. Google Maps 초기화 크래시 (Lifecycle Race Condition)
* **문제**: 앱 실행 직후 지도 화면 진입 시 `CameraUpdateFactory is not initialized` 에러와 함께 강제 종료 발생
* **원인**: Jetpack Compose의 빠른 렌더링으로 인해 Google Maps SDK 초기화 이전에 `LaunchedEffect`에서 카메라 이동 명령이 실행되는 타이밍 이슈 확인
* **해결**: `Application` 클래스의 `onCreate()`에서 `MapsInitializer`를 선행 호출하여 SDK 초기화 시점을 명확히 제어하고 지도 화면 진입 안정성 확보

### 2. BottomBar 네비게이션 스택 오염 문제
* **문제**: Home 탭에서 Full-Screen 화면 진입 후 다른 탭 이동 → 복귀 시 HomeMain이 아닌 이전 Full-Screen 화면이 복원되는 현상 발생
* **원인**: Navigation의 `saveState / restoreState` 동작 시 탭 내부 스택뿐 아니라 상위 화면까지 상태로 저장되는 구조적 문제
* **해결**: Full-Screen 화면 이동 시 `popUpTo` 옵션을 명확히 설정하고 탭 네비게이션과 독립적으로 관리되도록 네비게이션 그래프 분리

### 3. Firestore 실기기 동기화 지연 문제 (Offline Persistence)
* **문제**: 에뮬레이터에서는 정상 동작하나, 실기기에서 데이터 업로드가 장시간 지연되는 현상 발생
* **원인**: 코드 문제가 아닌 네트워크 환경 문제로 인해 Firestore가 오프라인 모드로 동작하며 데이터가 로컬 캐시에만 저장되고 있음을 확인
* **해결**: Firestore의 오프라인 동작 원리를 파악하고 네트워크 연결 상태를 체크하는 로직을 추가하여 테스트 환경 개선


