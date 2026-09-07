import type { ReactNode } from "react";

import chickenImage from "@/asset/chicken.png";
import saladImage from "@/asset/salad.png";

export interface AdSlide {
  id: string;
  eyebrow: string;
  title: ReactNode;
  description: ReactNode;
  /** 슬라이드 배경. 광고라 채도 높은 단색으로 둔다(그라디언트 금지). */
  backgroundClass: string;
  /** 배경 위 전경색. 배경마다 대비가 확보되는 값을 고른다. */
  foregroundClass: string;
  /** 일러스트. 새 의존성 없이 인라인 SVG 로 작도하고 색은 currentColor 로 상속받는다. */
  media: ReactNode;
}

function TicketArt() {
  return (
    <svg viewBox="0 0 80 80" role="presentation" className="size-full">
      <path
        d="M12 28a4 4 0 0 1 4-4h48a4 4 0 0 1 4 4v6a6 6 0 0 0 0 12v6a4 4 0 0 1-4 4H16a4 4 0 0 1-4-4v-6a6 6 0 0 0 0-12v-6Z"
        fill="currentColor"
        opacity="0.24"
      />
      <path
        d="M12 28a4 4 0 0 1 4-4h30v32H16a4 4 0 0 1-4-4v-6a6 6 0 0 0 0-12v-6Z"
        fill="currentColor"
      />
      <path
        d="m52 40 5 5 9-11"
        stroke="currentColor"
        strokeWidth="4"
        strokeLinecap="round"
        strokeLinejoin="round"
        fill="none"
      />
    </svg>
  );
}

/**
 * 사진 광고 미디어. 문구가 이미 내용을 다 말하므로 alt 는 비워 장식으로 둔다.
 *
 * 미디어 박스(80px)는 레이아웃 기준으로 그대로 두고, 사진만 1.5배로 키워
 * 박스 밖으로 넘치게 한다 — 모바일에서 잘리지 않으면서 사진을 크게 보인다.
 */
function SlideImage({ src }: { src: string }) {
  return (
    <img
      src={src}
      alt=""
      loading="lazy"
      className="size-full origin-right scale-150 object-contain"
    />
  );
}

export const AD_SLIDES: AdSlide[] = [
  {
    id: "intro",
    eyebrow: "신규 가입 시",
    title: (
      <>
        리뷰티켓
        <br />
        3장 자동지급
      </>
    ),
    description: <>리뷰 배지가 붙은 가게에서<br />무료 주문이 가능해요!</>,
    backgroundClass: "bg-brand-800",
    foregroundClass: "text-white",
    media: <TicketArt />,
  },
  {
    id: "salad",
    eyebrow: "오전 11시",
    title: "샐러드 20% 할인",
    description: <>오전 11시부터 샐러드 20%<br />할인 쿠폰 선착순 발급</>,
    backgroundClass: "bg-green-800",
    foregroundClass: "text-white",
    media: <SlideImage src={saladImage} />,
  },
  {
    id: "chicken",
    eyebrow: "오후 6시",
    title: "치킨 20% 할인",
    description: <>오후 6시부터 치킨 20%<br />할인 쿠폰 선착순 발급</>,
    backgroundClass: "bg-ink-900",
    foregroundClass: "text-white",
    media: <SlideImage src={chickenImage} />,
  },
];
