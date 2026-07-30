package com.moon.pharm.data.common

object FcmNotificationMessage {
    // 서버 전송용 메시지
    const val MSG_ANSWER_TITLE = "약사님 답변 도착! 💊"
    const val MSG_ANSWER_BODY = "회원님의 상담 질문에 답변이 등록되었습니다."

    const val MSG_NEW_CONSULT_TITLE = "새로운 상담 요청! 📝"
    const val MSG_NEW_CONSULT_BODY = "약사님, 답변을 기다리는 새로운 상담이 있습니다."

    // 에러 메시지
    const val ERR_UNKNOWN_SERVER = "알 수 없는 서버 오류"
    const val ERR_FCM_FAILED = "FCM 전송 실패: "
}
