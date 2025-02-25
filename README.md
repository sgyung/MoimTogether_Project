## 프로젝트 소개
- “MoimTogether”는 목적이 같은 사람들과 쉽고 빠르게 만남을 가질 수 있게 도와주기 위해 만든 프로젝트입니다. 해당 프로젝트에서는 모임 방을 생성하여 해당 모임에 관심이 있는 사람들이 모임에 가입해 만남이 이뤄질 수 있도록 만들었습니다. 또한, 해당 모임 장소의 리뷰 또는 좋아요를 확인할 수 있어서 장소를 선택하는데 있어서 도움을 줄 수 있습니다.

## 기술 스택
- Java17, SpringBoot 3.3.6, Spring Data JPA
- MySQL
- AWS(RDS, EC2, CodeDeploy, S3), Github Action

## 주요 기능
- **로그인 및 회원 가입 기능 구현**
    - SpringSecurity와 JWT을 이용한 로그인 구현
    
- **회원에 관련된 기능 구현**
    - 회원 삭제, 수정, 아이디 찾기, 비밀번호 찾기, 알림 확인, 모임 가입 목록, 탈퇴 등 회원에 관련된 기능을 구현.
    - 비밀번호 찾기는 회원의 이메일에 임시비밀번호를 발급하여 비밀번호를 변경할 수 있도록 구현.
    
- **모임에 관련된 기능 구현**
    - 모임 생성, 가입, 삭제, 공지사항 등록, 댓글 등록 등 모임과 관련된 기능을 구현.
    - 공지사항 등록은 모임장만 등록 가능 하도록 구현.
    - 모임 가입 시 발생하는 동시성 문제를 비관적 락 이용하여 구현.
    - 모임 삭제는 모임 내 가입 회원이 존재하지 않을 때만 가능하도록 구현.
    
- **장소에 관련된 기능 구현**
    - 외부 API를 이용하여 데이터를 스케줄러를 통해 주기적으로 DB에 저장하도록 기능 구현.
    - 좋아요 등록 시 발생하는 동시성 문제를 낙관적 락 이용하여 구현.
    
- **AWS EC2 + RDS 기반 인프라 구축**
    - **EC2** 인스턴스를 활용하여 애플리케이션 서버 배포
    - **RDS(MySQL)**를 사용하여 데이터베이스 관리 및 성능 최적화
    - 보안 강화를 위해 **EC2 보안 그룹 설정** 및 **RDS 접근 제어** 적용

- **CI/CD 자동화 (GitHub Actions + AWS CodeDeploy)**
    - **GitHub Actions**를 활용하여 코드 푸시 시 **자동 빌드 및 테스트** 실행
    - **AWS CodeDeploy**와 연계하여 **배포 자동화 및 무중단 배포(Blue-Green Deployment) 적용**
    - 배포 시 **애플리케이션 중단 없이 트래픽을 새로운 인스턴스로 전환**

- **Secret Manager를 활용한 보안 정보 관리**
    - **AWS Secret Manager**를 사용하여 **DB 비밀번호, API 키 등의 민감 정보 관리**
    - 환경 변수 대신 Secret Manager에서 **보안 정보를 안전하게 불러오는 방식 적용**

- **Route53을 활용한 DNS 설정 및 HTTPS 적용**
    - **AWS Route53**을 이용하여 **고정된 도메인(URL) 설정**
    - **Let's Encrypt SSL 인증서** 적용 및 **HTTPS 활성화**
    - Nginx에서 **HTTP → HTTPS 자동 리디렉션** 설정

- **무중단 배포 (Nginx + Blue-Green Deployment 적용)**
    - **Nginx를 리버스 프록시로 설정하여 트래픽 관리**
    - 배포 시 기존 인스턴스와 새로운 인스턴스를 분리하여 **Blue-Green Deployment 적용**
    - 배포 완료 후 **트래픽을 새로운 인스턴스로 전환**하여 **무중단 배포 구현**

# 기술적 고민
### **로그인 구현 방식 Session VS Token**
### 1. Session 방식
#### (1) 동작 방식
- 사용자가 로그인하면 서버에서 고유한 **세션 ID**를 생성하고, 이를 클라이언트에 **쿠키**로 전달합니다.
- 서버는 **세션 ID와 사용자 정보**를 세션 저장소(Session Store)에 저장하고, 클라이언트는 요청마다 쿠키를 포함해 인증합니다.
- 서버는 쿠키에 포함된 세션 ID를 세션 저장소와 대조하여 사용자를 인증합니다.

#### (2) 장점
- **높은 보안성**: 세션 ID는 서버에 저장되므로, 클라이언트 정보 탈취 시에도 서버에서 제어가 가능합니다.
- **사용자 상태 유지 (Stateful)**: 세션 저장소를 통해 사용자의 상태를 유지할 수 있습니다.
- **쿠키 기반 관리**: 브라우저가 쿠키를 자동 관리하므로 구현이 간단합니다.

#### (3) 단점
- **서버 자원 소모**: 사용자 수가 많아질수록 서버 메모리와 저장소 사용량이 증가합니다.
- **로드 밸런싱 문제**: 특정 서버에 저장된 세션 정보로 인해 Sticky Session이 필요합니다.
- **확장성 부족**: 대규모 시스템에서는 세션 관리 비용이 증가하여 확장에 한계가 있습니다.

### 2. Token 방식
#### (1) 동작 방식
- 사용자가 로그인하면 서버는 클라이언트에 **토큰 (예: JWT)**을 발급합니다.
- 토큰은 클라이언트에 저장(로컬 스토리지 또는 쿠키)되며, 요청 시 서버에 토큰을 포함해 인증 정보를 전달합니다.
- 서버는 토큰을 검증하여 사용자 인증을 수행하며, 토큰 자체에 사용자 정보를 포함할 수도 있습니다.

#### (2) 장점
- **Stateless**: 서버에서 세션 정보를 유지하지 않아 확장성과 유연성이 뛰어납니다.
- **로드 밸런싱에 유리**: 인증 정보를 클라이언트가 유지하므로, 요청이 어느 서버로 가더라도 인증이 가능합니다.
- **모바일 및 API 친화적**: 모바일 애플리케이션 및 API 인증에 적합합니다.

#### (3) 단점
- **토큰 탈취 위험**: 유출된 토큰을 제어하기 위해 추가적인 보안 메커니즘이 필요합니다.
- **토큰 크기**: 크기가 클 경우 네트워크 부하가 증가할 수 있습니다.
- **보안 관리 복잡성**: AccessToken과 RefreshToken을 조합하여 보안성을 강화하고, 토큰 무효화 처리를 구현해야 합니다.

### **3. 선택 이유**
- **확장성 및 성능**: Stateless 특성으로 서버 부담을 줄이고, 로드 밸런싱 및 오토 스케일링에 적합.
- **보안 보완**: 짧은 수명의 AccessToken과 RefreshToken을 통해 보안성을 강화.
- **결론**: 대규모 트래픽 환경과 유연한 서버 확장이 필요한 시스템에 적합해 JWT 방식 채택.
---
### 외부 API를 요청마다 호출하지 않고 스케줄러를 활용한 이유
- **스케줄러 활용 배경**
  - 클라이언트 요청마다 외부 API를 호출하는 대신, 스케줄러를 활용해 데이터를 주기적으로 수집하고 로컬 데이터베이스에 저장하는 방식을 채택.
  - 이는 성능, 안정성, 비용 측면에서 더 효율적이고 안정적인 솔루션을 제공.
- **성능 최적화**
  - 클라이언트 요청 시마다 외부 API를 호출하면 응답 지연과 서버 부하 발생 가능.
  - 데이터를 미리 로컬 데이터베이스에 저장함으로써 클라이언트 요청 시 빠르게 응답 가능.
- **안정성 강화**
  - 외부 API 서버 장애나 네트워크 불안정 상황에서도, 미리 수집된 데이터를 사용해 안정적인 서비스 제공 가능.
  - 시스템 신뢰성을 높이는 데 기여.
- **비용 절감**
  - 외부 API 호출 횟수에 따른 비용 부담 감소.
  - 스케줄러 기반 방식으로 불필요한 API 호출을 줄여 효율성 확보.
- **스케줄러 주기 설정**
  - 외부 API 데이터 갱신 주기가 매일 이루어지는 점을 고려해, 하루에 한 번 스케줄러 실행 설정.
  - 실시간성이 크게 요구되지 않는 환경에서 효율성과 안정성을 극대화.
- **결론**
  - 스케줄러를 활용한 주기적 데이터 수집 방식은 성능 최적화, 안정성 강화, 비용 절감 측면에서 최적의 선택.
  - 이를 통해 클라이언트는 빠르고 안정적인 응답을 받을 수 있었으며, 시스템은 확장성과 비용 효율성을 동시에 확보.

## 트러블 슈팅
### 1. 모임 가입과 좋아요 기능에서 동시성 문제 발생

### 1-1. 모임 가입의 동시성 문제

**문제 상황**  
- 모임 가입 시 정원이 초과되는 상황에서도 여러 가입 요청이 동시에 처리되어 정원보다 많은 인원이 가입되는 문제가 발생.

**해결 방안**  
- **Synchronized 적용 및 문제점**  
  초기에는 Synchronized를 사용해 동시성 문제를 제어하려 했으나, 트랜잭션 Commit 전에 락이 해제되어 갱신 손실 문제가 발생.  
- **비관적 락(Pessimistic Lock) 적용**  
  충돌 가능성이 높은 상황이므로 비관적 락을 적용해 특정 트랜잭션만 레코드에 접근하도록 설정. 이를 통해 갱신 손실 문제를 방지.

**도입 후 성과**  
- 비관적 락 도입으로 정원을 초과하는 가입 요청 문제를 방지.  
- 데이터 정합성과 무결성을 유지하며, 사용자에게 신뢰성 있는 서비스 제공.

---

### 1-2. 좋아요 기능의 동시성 문제

**문제 상황**  
- 좋아요 기능에서 동시에 여러 요청이 처리되면서 좋아요 수가 올바르지 않게 저장되는 문제가 발생.

**해결 방안**  
- **낙관적 락 적용 및 데드락 문제 발생**  
  충돌 빈도가 낮다고 판단해 낙관적 락을 적용했으나, MySQL의 외래 키 제약 조건으로 인해 데드락 발생.  
- **재시도 로직 도입**  
  트랜잭션 충돌 시 롤백된 작업을 재시도하는 로직을 추가하여 데드락 문제를 완화하고 안정성을 확보.

**도입 후 성과**  
- 재시도 로직 도입으로 데드락 문제 해결.  
- 데이터 정합성과 무결성을 유지하며 안정적인 트랜잭션 처리 가능.

---

## 2. LazyInitializationException 예외 발생

**문제 상황**  
- 모임 생성 과정에서 Lazy 로딩된 회원 엔티티가 트랜잭션 외부에서 조회되면서 **LazyInitializationException** 발생.

**해결 방안**  
- 트랜잭션 내부에서 회원 엔티티를 직접 조회해 영속 상태로 변환.  
- 이를 통해 Lazy 로딩이 정상적으로 이루어지도록 설정.

**도입 후 성과**  
- Lazy 로딩 문제 해결로 안정적인 데이터 로딩 가능.  
- 모임 생성 기능에서 발생하던 예외 문제를 해결하며 시스템 신뢰성 향상.

---

## 3. Delete 쿼리가 나가지 않는 문제

**문제 상황**  
- 회원과 모임 간 **다대다 관계**를 중간 테이블로 매핑했으나, 회원 탈퇴 시 중간 테이블 데이터가 삭제되지 않는 문제가 발생.  
- 부모(회원/모임) 엔티티가 자식(회원_모임) 엔티티를 참조하고 있어 JPA가 삭제를 수행하지 않음.

**해결 방안**  
1. **명시적 관계 제거 방식**  
   - 부모 엔티티에서 자식 엔티티와의 관계를 명시적으로 제거.  
   - 하지만 코드 복잡도가 증가해 유지보수에 어려움 발생.  
2. **Cascade와 orphanRemoval 도입**  
   - 부모 엔티티에 **orphanRemoval = true**를 설정해 고아 객체로 간주된 데이터를 자동 삭제.  
   - Cascade 옵션으로 부모 엔티티 변경 사항을 자식 엔티티에 전파.

**도입 후 성과**  
- **orphanRemoval**과 **Cascade**로 삭제 로직을 간소화.  
- 데이터 무결성을 유지하며 안정적이고 효율적인 데이터 관리 가능.

---

## 4. JWT 토큰 적용 후 403 에러 발생

**문제 상황**  
- JWT 토큰 적용 후 클라이언트 요청이 403 에러와 함께 NullPointerException을 유발.  
- 문제 원인은 인증 객체 생성 시 권한 정보(Authorities)가 null로 설정된 점.

**해결 방안**  
1. **문제 원인 분석**  
   - 인증 객체(Authentication)에 권한 정보가 없으면 유효하지 않은 상태로 간주.  
   - 이를 방지하기 위해 권한이 없는 경우 빈 리스트를 전달.
   - 하지만, 이후 시스템 확장성을 고려해 기본적으로 USER 권한(Role)을 부여하는 방식을 도입.  
2. **해결 방안 적용**  
   - UsernamePasswordAuthenticationToken 생성 시, 권한 정보에 기본적으로 ROLE_USER를 포함한 리스트를 전달.  
   - 이를 통해 권한 기반 인증 처리가 유연해지고, 추가적인 권한 요구 사항이 생길 경우 쉽게 확장 가능하도록 설계.

**도입 후 성과**  
- 권한이 없는 상태에서도 기본 ROLE_USER 권한을 부여해 인증 객체를 유효하게 유지.  
- 403 에러와 NullPointerException 문제를 해결하고, 확장 가능한 인증 구조를 구축.
- 권한 기반 접근 제어를 유연하게 처리할 수 있어 향후 기능 확장과 유지보수가 용이해짐.

## API문서
https://www.notion.so/144d6f74e9bc81a39435d21e2d997917?v=144d6f74e9bc8107a913000cf98e0627&pvs=4

## 아키텍처
![Image](https://github.com/user-attachments/assets/2c3bae0b-d768-4d45-8fd8-67de270c0cc1)

## ERD
![moim (1)](https://github.com/user-attachments/assets/d325ba6f-6aad-45fe-bb7c-3f74fe97aa83)
