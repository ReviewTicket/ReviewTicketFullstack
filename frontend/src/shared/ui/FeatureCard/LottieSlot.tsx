import { Lottie } from "lottie-react";
import foodAnimation from "@/asset/Food.json";

export interface LottieSlotProps {
  /** 스크린리더가 읽을 대체 문구. 장식이면 비워 둔다 */
  label?: string;
  className?: string;
}

/**
 * Lottie 애니메이션이 들어갈 자리.
 *
 * 바깥 치수(aspect-square, 배경, 라운드)는 그대로 둔다 — FeatureCard 의 media
 * 슬롯 기본값이라 여기 치수가 바뀌면 카드 레이아웃이 흔들린다.
 */
export function LottieSlot({ label = "", className = "" }: LottieSlotProps) {
  return (
    <div
      role={label ? "img" : "presentation"}
      aria-label={label || undefined}
      className={`flex aspect-square w-full items-center justify-center overflow-hidden rounded-xl bg-brand-50 ${className}`}
    >
      <Lottie
        src={foodAnimation}
        loop
        autoplay
        className="h-full w-full"
      />
    </div>
  );
}
