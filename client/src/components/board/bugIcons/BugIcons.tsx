import React from 'react';
import type {Bug} from "../../../types/board";

import BeeIcon from "./BeeIcon.tsx";
import BeetleIcon from "./BeetleIcon.tsx";
import GrasshopperIcon from "./GrasshopperIcon.tsx";
import SpiderIcon from "./SpiderIcon.tsx";
import AntIcon from "./AntIcon.tsx";
import LadybugIcon from "./LadybugIcon.tsx";
import MosquitoIcon from "./MosquitoIcon.tsx";
import WoodlouseIcon from "./WoodlouseIcon.tsx";

export type IconProps = React.SVGProps<SVGGElement>;

export const baseIcon: IconProps = {
    fill: 'none',
    stroke: 'currentColor',
    strokeWidth: 1.6,
    strokeLinecap: 'round',
    strokeLinejoin: 'round',
};

export const BUG_ICONS: Record<Bug, React.ComponentType<IconProps>> = {
    BEE: BeeIcon,
    BEETLE: BeetleIcon,
    GRASSHOPPER: GrasshopperIcon,
    SPIDER: SpiderIcon,
    ANT: AntIcon,
    LADYBUG: LadybugIcon,
    MOSQUITO: MosquitoIcon,
    WOODLOUSE: WoodlouseIcon,
};