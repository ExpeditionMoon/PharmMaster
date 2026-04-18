package com.moon.pharm.data.datasource.remote.ai

object DdiAiConst {
    const val MODEL_NAME = "gemini-1.5-flash"

    fun buildAnalyzeDdiPrompt(drugListText: String): String = """
        환자가 다음 약물들을 함께 복용하려고 합니다: [$drugListText]
        이 약물들 간의 상호작용(DDI)과 병용 금기 사항을 분석해 주세요.
        반드시 아래의 JSON 형식으로만 답변을 반환하세요:
        {
          "riskLevel": "HIGH" | "MODERATE" | "LOW" | "UNKNOWN",
          "interactionSummary": "분석 결과에 대한 1~2줄 요약",
          "details": ["상세 주의사항 1", "상세 주의사항 2"]
        }
    """.trimIndent()

    fun buildExtractDrugNamesPrompt(safeOcrText: String): String = """
        다음 텍스트는 환자의 처방전 또는 약 봉투를 OCR로 스캔한 결과이며, 민감정보는 마스킹 처리되었습니다.
        인식 오류로 인한 오타나 불필요한 정보(병원 이름, 복약 시간, 식전/식후 등)가 포함되어 있을 수 있습니다.
        이 텍스트에서 오직 '의약품 이름'만 정확하게 추론하고 정제하여 문자열 배열(JSON Array) 형태로 반환해 주세요.
        약 이름이 아닌 것은 모두 제외하세요. (예: "타이레놀정500mg", "아목시실린캡슐")

        [OCR 텍스트 시작]
        $safeOcrText
        [OCR 텍스트 끝]

        반드시 아래의 JSON 배열 형식으로만 답변을 반환하세요:
        ["약이름1", "약이름2", "약이름3"]
    """.trimIndent()
}