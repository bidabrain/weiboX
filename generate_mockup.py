#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
generate_mockup.py — WeiboX UI mockup generator
Run:   python3 generate_mockup.py
Output: docs/ui_mockup.png
"""

from PIL import Image, ImageDraw, ImageFont
import os

# ─── Theme colors (WeiboX Color.kt) ──────────────────────────────────────────
BG          = (242, 242, 247)
SURFACE     = (255, 255, 255)
PRIMARY     = (250, 125,  64)
PRIMARY_CTR = (255, 237, 227)
ON_SURFACE  = ( 26,  26,  26)
SECONDARY   = (102, 102, 102)
HINT        = (185, 185, 185)
OUTLINE     = (229, 229, 229)

AV = [
    (250, 175, 140),
    (140, 195, 250),
    (160, 225, 165),
    (250, 175, 205),
    (195, 165, 250),
    (250, 215, 140),
]

# ─── Screen geometry ──────────────────────────────────────────────────────────
PW, PH    = 390, 844
STATUS_H  = 28
TOPBAR_H  = 56
NAV_H     = 72
CONTENT_Y = STATUS_H + TOPBAR_H

# ─── Fonts ────────────────────────────────────────────────────────────────────
def load_font(size):
    for path in [
        "/System/Library/Fonts/PingFang.ttc",
        "/System/Library/Fonts/Helvetica.ttc",
        "/Library/Fonts/Arial Unicode MS.ttf",
    ]:
        if os.path.exists(path):
            try:
                return ImageFont.truetype(path, size, index=0)
            except Exception:
                continue
    return ImageFont.load_default()

Fs = {s: load_font(s) for s in [10, 11, 12, 13, 14, 15, 16, 19, 22, 24]}

# ─── Drawing helpers ──────────────────────────────────────────────────────────

def rrect(draw, x0, y0, x1, y1, r=8, fill=None, outline=None, lw=1):
    draw.rounded_rectangle([x0, y0, x1, y1], radius=r, fill=fill,
                            outline=outline, width=lw)

def hline(draw, y, x0=0, x1=PW, color=OUTLINE):
    draw.line([(x0, y), (x1, y)], fill=color, width=1)

def tw(draw, text, f):
    bb = draw.textbbox((0, 0), text, font=f)
    return bb[2] - bb[0]

def th(draw, text, f):
    bb = draw.textbbox((0, 0), text, font=f)
    return bb[3] - bb[1]

def ctext(draw, text, cx, y, f, fill=ON_SURFACE):
    w = tw(draw, text, f)
    draw.text((cx - w // 2, y), text, fill=fill, font=f)

def clip_text(draw, text, max_w, f):
    while len(text) > 1:
        if tw(draw, text, f) <= max_w:
            break
        text = text[:-1]
    return text

def avatar(draw, cx, cy, r, color, letter):
    draw.ellipse([cx - r, cy - r, cx + r, cy + r], fill=color)
    f = Fs[14]
    w, h = tw(draw, letter, f), th(draw, letter, f)
    draw.text((cx - w // 2, cy - h // 2 - 1), letter, fill=(70, 50, 40), font=f)

# ─── Shared UI components ─────────────────────────────────────────────────────

def status_bar(draw):
    draw.rectangle([0, 0, PW, STATUS_H], fill=SURFACE)
    draw.text((14, 7), "9:41", fill=ON_SURFACE, font=Fs[12])
    bx, by = PW - 38, 8
    draw.rectangle([bx, by, bx + 22, by + 12], outline=SECONDARY, width=1)
    draw.rectangle([bx + 22, by + 3, bx + 24, by + 9], fill=SECONDARY)
    draw.rectangle([bx + 1, by + 1, bx + 15, by + 11], fill=ON_SURFACE)

def top_bar(draw, title, back=False, right=None):
    y = STATUS_H
    draw.rectangle([0, y, PW, y + TOPBAR_H], fill=SURFACE)
    x = 54 if back else 16
    h = th(draw, title, Fs[19])
    draw.text((x, y + (TOPBAR_H - h) // 2), title, fill=ON_SURFACE, font=Fs[19])
    if back:
        ax, ay = 22, y + TOPBAR_H // 2
        draw.line([(ax + 9, ay - 9), (ax + 1, ay), (ax + 9, ay + 9)],
                  fill=ON_SURFACE, width=2)
    if right:
        rw = tw(draw, right, Fs[14])
        draw.text((PW - 16 - rw, y + (TOPBAR_H - th(draw, right, Fs[14])) // 2),
                  right, fill=PRIMARY, font=Fs[14])
    hline(draw, y + TOPBAR_H)

def nav_bar(draw, active=0):
    y = PH - NAV_H
    draw.rectangle([0, y, PW, PH], fill=SURFACE)
    hline(draw, y)
    tabs   = ["时间线", "内容发现", "关注", "设置"]
    icons  = ["≡", "○", "♡", "☆"]
    slot_w = PW // 4
    for i, (label, icon) in enumerate(zip(tabs, icons)):
        cx = i * slot_w + slot_w // 2
        c  = PRIMARY if i == active else HINT
        if i == active:
            rrect(draw, cx - 26, y + 5, cx + 26, y + 24, r=10, fill=PRIMARY_CTR)
        iw = tw(draw, icon, Fs[15])
        draw.text((cx - iw // 2, y + 7), icon, fill=c, font=Fs[15])
        lw = tw(draw, label, Fs[10])
        draw.text((cx - lw // 2, y + 29), label, fill=c, font=Fs[10])

# ─── Screen 1: 时间线 ─────────────────────────────────────────────────────────

def screen_home():
    img  = Image.new("RGB", (PW, PH), BG)
    draw = ImageDraw.Draw(img)
    status_bar(draw)
    top_bar(draw, "时间线")
    nav_bar(draw, 0)

    posts = [
        ("科", AV[0], "科技新闻",  "2分钟前",
         "OpenAI 发布新模型，多模态能力大幅提升，推理速度提升 3 倍，引发行业广泛关注。",
         False, "1.2万", "234", "567"),
        ("生", AV[1], "生活日记",  "15分钟前",
         "今天天气真好，出去走走～阳光正好，微风不燥",
         True,  "89",   "12",  "5"),
        ("体", AV[2], "体育快讯",  "1小时前",
         "世界杯决赛精彩瞬间！最后时刻绝杀，全场沸腾 🏆",
         True,  "5.6万","3421","8901"),
    ]

    y = CONTENT_Y + 8
    for initial, av_c, name, time_str, body, has_img, likes, cmts, rpts in posts:
        card_h = 158 if has_img else 116
        draw.rectangle([0, y, PW, y + card_h], fill=SURFACE)

        avatar(draw, 32, y + 22, 18, av_c, initial)
        draw.text((58, y + 10), name, fill=ON_SURFACE, font=Fs[15])
        rw = tw(draw, time_str, Fs[12])
        draw.text((PW - 16 - rw, y + 13), time_str, fill=HINT, font=Fs[12])

        body_clipped = clip_text(draw, body, PW - 32, Fs[13])
        draw.text((16, y + 44), body_clipped, fill=ON_SURFACE, font=Fs[13])

        if has_img:
            rrect(draw, 16, y + 66, PW - 16, y + 124, r=6, fill=(232, 232, 238))
            ctext(draw, "图片", PW // 2, y + 86, Fs[13], fill=HINT)

        ay = y + card_h - 24
        for j, (ic, val) in enumerate([("♡", likes), ("◯", cmts), ("↺", rpts)]):
            draw.text((16 + j * 120, ay), f"{ic}  {val}", fill=SECONDARY, font=Fs[12])

        draw.rectangle([0, y + card_h, PW, y + card_h + 8], fill=BG)
        y += card_h + 8

    return img

# ─── Screen 2: 搜索 ───────────────────────────────────────────────────────────

def screen_search():
    img  = Image.new("RGB", (PW, PH), BG)
    draw = ImageDraw.Draw(img)
    status_bar(draw)
    top_bar(draw, "内容发现")
    nav_bar(draw, 1)

    sy = CONTENT_Y + 12
    rrect(draw, 16, sy, PW - 16, sy + 44, r=10, fill=SURFACE, outline=OUTLINE)
    draw.text((36, sy + 13), "搜索用户名或数字 ID…", fill=HINT, font=Fs[13])
    draw.text((PW - 46, sy + 13), "⊙", fill=PRIMARY, font=Fs[14])
    hline(draw, sy + 58)

    users = [
        ("陈", AV[3], "陈小美", "设计师 / 生活记录者",     False),
        ("王", AV[4], "王大锤", "科技评测 | 数码爱好者",   True),
        ("林", AV[5], "林观察", "时事评论 · 独立撰稿人",   False),
        ("张", AV[0], "张小北", "电影导演 / 编剧",         True),
    ]

    y = sy + 66
    for initial, av_c, name, bio, followed in users:
        draw.rectangle([0, y, PW, y + 66], fill=SURFACE)
        avatar(draw, 38, y + 33, 22, av_c, initial)
        draw.text((72, y + 13), name, fill=ON_SURFACE, font=Fs[15])
        bio_c = clip_text(draw, bio, 190, Fs[12])
        draw.text((72, y + 36), bio_c, fill=SECONDARY, font=Fs[12])
        if followed:
            rrect(draw, PW - 82, y + 18, PW - 14, y + 46, r=14, fill=OUTLINE)
            ctext(draw, "已关注", PW - 48, y + 25, Fs[12], fill=SECONDARY)
        else:
            rrect(draw, PW - 82, y + 18, PW - 14, y + 46, r=14, fill=PRIMARY)
            ctext(draw, "+ 关注", PW - 48, y + 25, Fs[12], fill=SURFACE)
        hline(draw, y + 66)
        y += 66

    return img

# ─── Screen 3: 关注 ───────────────────────────────────────────────────────────

def screen_following():
    img  = Image.new("RGB", (PW, PH), BG)
    draw = ImageDraw.Draw(img)
    status_bar(draw)
    top_bar(draw, "关注")
    nav_bar(draw, 2)

    users = [
        ("陈", AV[3], "陈小美", "设计师 / 生活记录者",     "4.2万 粉丝"),
        ("王", AV[4], "王大锤", "科技评测 | 数码爱好者",   "12.1万 粉丝"),
        ("林", AV[5], "林观察", "时事评论 · 独立撰稿人",   "31.8万 粉丝"),
        ("张", AV[0], "张小北", "电影导演 / 编剧",         "8.9万 粉丝"),
        ("李", AV[1], "李明远", "旅行摄影师，走遍三十国",  "22.4万 粉丝"),
    ]

    y = CONTENT_Y + 4
    for initial, av_c, name, bio, fans in users:
        draw.rectangle([0, y, PW, y + 70], fill=SURFACE)
        avatar(draw, 38, y + 35, 22, av_c, initial)
        draw.text((72, y + 12), name, fill=ON_SURFACE, font=Fs[15])
        bio_c = clip_text(draw, bio, 185, Fs[12])
        draw.text((72, y + 34), bio_c, fill=SECONDARY, font=Fs[12])
        draw.text((72, y + 52), fans, fill=HINT, font=Fs[11])
        rrect(draw, PW - 88, y + 20, PW - 14, y + 48, r=14, fill=OUTLINE)
        ctext(draw, "✓ 已关注", PW - 51, y + 28, Fs[11], fill=SECONDARY)
        hline(draw, y + 70)
        y += 70

    return img

# ─── Screen 4: 设置 ───────────────────────────────────────────────────────────

def screen_settings():
    img  = Image.new("RGB", (PW, PH), BG)
    draw = ImageDraw.Draw(img)
    status_bar(draw)
    top_bar(draw, "设置")
    nav_bar(draw, 3)

    y = CONTENT_Y + 14

    def section_title(t):
        nonlocal y
        draw.text((16, y), t, fill=PRIMARY, font=Fs[13])
        y += 24

    def input_field(label, value="", placeholder=""):
        nonlocal y
        draw.text((16, y), label, fill=SECONDARY, font=Fs[11])
        y += 16
        rrect(draw, 16, y, PW - 16, y + 40, r=8, fill=SURFACE, outline=OUTLINE)
        txt = value if value else placeholder
        col = ON_SURFACE if value else HINT
        draw.text((28, y + 12), txt, fill=col, font=Fs[13])
        y += 48

    def toggle_row(label, on):
        nonlocal y
        draw.rectangle([0, y, PW, y + 50], fill=SURFACE)
        draw.text((16, y + 15), label, fill=ON_SURFACE, font=Fs[14])
        tx = PW - 62
        rrect(draw, tx, y + 14, tx + 46, y + 36, r=11,
              fill=PRIMARY if on else OUTLINE)
        cx = tx + 36 if on else tx + 10
        draw.ellipse([cx - 10, y + 15, cx + 10, y + 35], fill=SURFACE)
        hline(draw, y + 50)
        y += 54

    def button(label, style="filled"):
        nonlocal y
        if style == "filled":
            rrect(draw, 16, y, PW - 16, y + 42, r=8, fill=OUTLINE)
            ctext(draw, label, PW // 2, y + 12, Fs[14], fill=SECONDARY)
        else:
            rrect(draw, 16, y, PW - 16, y + 42, r=8,
                  fill=SURFACE, outline=PRIMARY)
            ctext(draw, label, PW // 2, y + 12, Fs[14], fill=PRIMARY)
        y += 50

    # ── Cookie
    section_title("登录 Cookie")
    rrect(draw, 16, y, PW - 16, y + 68, r=8, fill=SURFACE, outline=OUTLINE)
    draw.text((28, y + 8),  "微博 Cookie",   fill=SECONDARY, font=Fs[11])
    draw.text((28, y + 26), "SUB=_2A25Kxxxxxxx…", fill=ON_SURFACE, font=Fs[13])
    y += 76
    draw.ellipse([16, y + 3, 26, y + 13], fill=PRIMARY)
    draw.text((32, y + 1), "已配置 Cookie（完整模式）", fill=PRIMARY, font=Fs[12])
    y += 20
    button("已保存", "filled")
    hline(draw, y); y += 14

    # ── 外观
    section_title("外观")
    toggle_row("深色模式", on=False)
    hline(draw, y); y += 14

    # ── 支持开发者
    section_title("支持开发者")
    qr = 90
    qx = PW // 2 - qr // 2
    rrect(draw, qx, y, qx + qr, y + qr, r=4, fill=SURFACE, outline=OUTLINE)
    # QR pattern hint
    cell = 8
    pattern = [
        (1,1),(2,1),(3,1),(1,2),(3,2),(1,3),(2,3),(3,3),
        (5,1),(6,1),(7,1),(5,2),(7,2),(5,3),(6,3),(7,3),
        (1,5),(3,5),(2,6),(4,6),(6,6),(1,7),(2,7),(3,7),
    ]
    for gx, gy in pattern:
        draw.rectangle([qx + 6 + gx*cell, y + 6 + gy*cell,
                        qx + 6 + gx*cell + cell - 1,
                        y + 6 + gy*cell + cell - 1], fill=ON_SURFACE)
    y += qr + 8
    button("↓  保存二维码到相册", "outline")
    hline(draw, y); y += 14

    # ── 关于
    section_title("关于")
    draw.text((16, y), "WeiboX  v1.0", fill=SECONDARY, font=Fs[13])

    return img

# ─── Compose ─────────────────────────────────────────────────────────────────

def compose():
    screens = [
        ("① 时间线",    screen_home()),
        ("② 内容发现",  screen_search()),
        ("③ 关注",      screen_following()),
        ("④ 设置",      screen_settings()),
    ]

    SCALE    = 0.56
    SW, SH   = int(PW * SCALE), int(PH * SCALE)
    GAP      = 28
    MARGIN   = 36
    HEADER_H = 78

    W = MARGIN + SW + GAP + SW + MARGIN
    H = HEADER_H + MARGIN + SH + GAP + SH + MARGIN + 28

    canvas = Image.new("RGB", (W, H), BG)
    draw   = ImageDraw.Draw(canvas)

    # Header bar
    draw.rectangle([0, 0, W, HEADER_H], fill=SURFACE)
    title  = "WeiboX  —  界面预览"
    f_head = load_font(22)
    ctext(draw, title, W // 2, (HEADER_H - th(draw, title, f_head)) // 2, f_head)
    hline(draw, HEADER_H, x0=0, x1=W)

    positions = [
        (MARGIN,           HEADER_H + MARGIN),
        (MARGIN + SW + GAP, HEADER_H + MARGIN),
        (MARGIN,           HEADER_H + MARGIN + SH + GAP),
        (MARGIN + SW + GAP, HEADER_H + MARGIN + SH + GAP),
    ]

    f_cap = load_font(12)
    for (label, img), (px, py) in zip(screens, positions):
        scaled = img.resize((SW, SH), Image.LANCZOS)

        # drop shadow
        for d in range(5, 0, -1):
            alpha = 210 - d * 35
            shadow_color = (alpha, alpha, alpha + 5)
            rrect(draw, px + d, py + d, px + SW + d, py + SH + d,
                  r=16, fill=shadow_color)

        # phone frame
        rrect(draw, px - 2, py - 2, px + SW + 2, py + SH + 2,
              r=17, fill=(210, 210, 215))
        canvas.paste(scaled, (px, py))

        # caption
        ctext(draw, label, px + SW // 2, py + SH + 10, f_cap, fill=SECONDARY)

    # Footer
    f_foot = load_font(11)
    foot = "基于 m.weibo.cn 移动端接口  ·  参考 dataabc/weibo-crawler"
    ctext(draw, foot, W // 2, H - 20, f_foot, fill=HINT)

    return canvas

# ─── Main ─────────────────────────────────────────────────────────────────────

if __name__ == "__main__":
    os.makedirs("docs", exist_ok=True)
    img = compose()
    out = "docs/ui_mockup.png"
    img.save(out, "PNG")
    print(f"✓ 已保存 → {out}  ({img.width}×{img.height})")
