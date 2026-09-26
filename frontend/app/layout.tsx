import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Quản lý Niên Khóa - Hệ Thống Đào Tạo & Chuẩn Đầu Ra",
  description: "Hệ thống quản lý môn học, niên khóa và đánh giá chuẩn đầu ra",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="vi" className="h-full" suppressHydrationWarning>
      <body className="h-full font-sans text-slate-700 dark:text-slate-200 antialiased overflow-hidden flex bg-slate-50 dark:bg-slate-900 transition-colors duration-200">
        {children}
      </body>
    </html>
  );
}
