"use client";

import React, { useState, useEffect, useMemo } from "react";

interface AcademicYear {
  id: number;
  code: string;
  description: string;
  startYear: number;
  endYear: number;
  status: "Sắp tuyển sinh" | "Chuẩn bị nhập học" | "Đang đào tạo" | "Đã tốt nghiệp" | "Chưa mở" | string;
  isLocked: boolean;
}

const INITIAL_DATA: AcademicYear[] = [
  { id: 1, code: "K2026", description: "Lớp sinh viên niên khóa 2026 - 2030", startYear: 2026, endYear: 2030, status: "Sắp tuyển sinh", isLocked: false },
  { id: 2, code: "K2025", description: "Lớp sinh viên niên khóa 2025 - 2029", startYear: 2025, endYear: 2029, status: "Chuẩn bị nhập học", isLocked: false },
  { id: 3, code: "K2024", description: "Lớp sinh viên niên khóa 2024 - 2028", startYear: 2024, endYear: 2028, status: "Đang đào tạo", isLocked: false },
  { id: 4, code: "K2023", description: "Lớp sinh viên niên khóa 2023 - 2027", startYear: 2023, endYear: 2027, status: "Đang đào tạo", isLocked: false },
  { id: 5, code: "K2022", description: "Lớp sinh viên niên khóa 2022 - 2026", startYear: 2022, endYear: 2026, status: "Đang đào tạo", isLocked: false },
  { id: 6, code: "K2021", description: "Lớp sinh viên niên khóa 2021 - 2025", startYear: 2021, endYear: 2025, status: "Đang đào tạo", isLocked: false },
  { id: 7, code: "K2020", description: "Lớp sinh viên niên khóa 2020 - 2024", startYear: 2020, endYear: 2024, status: "Đã tốt nghiệp", isLocked: true },
  { id: 8, code: "K2019", description: "Lớp sinh viên niên khóa 2019 - 2023", startYear: 2019, endYear: 2023, status: "Đã tốt nghiệp", isLocked: true },
  { id: 9, code: "K2018", description: "Lớp sinh viên niên khóa 2018 - 2022", startYear: 2018, endYear: 2022, status: "Đã tốt nghiệp", isLocked: true },
  { id: 10, code: "K2017", description: "Lớp sinh viên niên khóa 2017 - 2021", startYear: 2017, endYear: 2021, status: "Đã tốt nghiệp", isLocked: true },
  { id: 11, code: "K2016", description: "Lớp sinh viên niên khóa 2016 - 2020", startYear: 2016, endYear: 2020, status: "Đã tốt nghiệp", isLocked: true },
  { id: 12, code: "K2015", description: "Lớp sinh viên niên khóa 2015 - 2019", startYear: 2015, endYear: 2019, status: "Đã tốt nghiệp", isLocked: true },
  { id: 13, code: "K2027 (Dự kiến)", description: "Lớp sinh viên niên khóa 2027 - 2031", startYear: 2027, endYear: 2031, status: "Chưa mở", isLocked: false },
];

const SCHOOL_YEARS = ["2025-2026", "2024-2025", "2023-2024", "2022-2023", "2021-2022"];

export default function AcademicYearPage() {
  const [data, setData] = useState<AcademicYear[]>(INITIAL_DATA);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedSchoolYearIndex, setSelectedSchoolYearIndex] = useState(2); // 2023 - 2024
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [activeMenu, setActiveMenu] = useState("Quản lý niên khóa");

  // Sorting state
  const [sortField, setSortField] = useState<keyof AcademicYear | null>(null);
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");

  // Filter state
  const [statusFilter, setStatusFilter] = useState<string>("Tất cả");
  const [showStatusFilterMenu, setShowStatusFilterMenu] = useState(false);

  // Modals
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<"create" | "edit" | "view">("create");
  const [currentCohort, setCurrentCohort] = useState<AcademicYear | null>(null);
  const [formData, setFormData] = useState({
    code: "",
    description: "",
    startYear: new Date().getFullYear(),
    endYear: new Date().getFullYear() + 4,
    status: "Đang đào tạo",
  });

  // Delete confirmation
  const [deleteConfirmId, setDeleteConfirmId] = useState<number | null>(null);

  // Toast alert
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => {
      setToastMessage(null);
    }, 3000);
  };

  // Dark mode init & toggle
  useEffect(() => {
    const isDark =
      localStorage.getItem("theme") === "dark" ||
      (!("theme" in localStorage) && window.matchMedia("(prefers-color-scheme: dark)").matches);
    setIsDarkMode(isDark);
    if (isDark) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, []);

  const toggleTheme = () => {
    const nextDark = !isDarkMode;
    setIsDarkMode(nextDark);
    if (nextDark) {
      document.documentElement.classList.add("dark");
      localStorage.setItem("theme", "dark");
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.setItem("theme", "light");
    }
  };

  // School year changer
  const handlePrevYear = () => {
    if (selectedSchoolYearIndex > 0) {
      setSelectedSchoolYearIndex(selectedSchoolYearIndex - 1);
    }
  };

  const handleNextYear = () => {
    if (selectedSchoolYearIndex < SCHOOL_YEARS.length - 1) {
      setSelectedSchoolYearIndex(selectedSchoolYearIndex + 1);
    }
  };

  // Sort handler
  const handleSort = (field: keyof AcademicYear) => {
    if (sortField === field) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortField(field);
      setSortOrder("asc");
    }
  };

  // Lock / Unlock toggle
  const handleToggleLock = (id: number) => {
    setData((prev) =>
      prev.map((item) => {
        if (item.id === id) {
          const nextLock = !item.isLocked;
          showToast(`Đã ${nextLock ? "khóa" : "mở khóa"} niên khóa ${item.code}`);
          return { ...item, isLocked: nextLock };
        }
        return item;
      })
    );
  };

  // Delete handler
  const handleDelete = (id: number) => {
    const itemToDelete = data.find((d) => d.id === id);
    setData((prev) => prev.filter((item) => item.id !== id));
    setDeleteConfirmId(null);
    showToast(`Đã xóa niên khóa ${itemToDelete?.code || ""}`);
  };

  // Open Create Modal
  const handleOpenCreate = () => {
    setModalMode("create");
    setFormData({
      code: "",
      description: "",
      startYear: new Date().getFullYear(),
      endYear: new Date().getFullYear() + 4,
      status: "Sắp tuyển sinh",
    });
    setIsModalOpen(true);
  };

  // Open Edit Modal
  const handleOpenEdit = (item: AcademicYear) => {
    setModalMode("edit");
    setCurrentCohort(item);
    setFormData({
      code: item.code,
      description: item.description,
      startYear: item.startYear,
      endYear: item.endYear,
      status: item.status,
    });
    setIsModalOpen(true);
  };

  // Open View Modal
  const handleOpenView = (item: AcademicYear) => {
    setModalMode("view");
    setCurrentCohort(item);
    setIsModalOpen(true);
  };

  // Form submit
  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.code.trim()) {
      alert("Vui lòng nhập mã niên khóa!");
      return;
    }
    if (formData.endYear < formData.startYear) {
      alert("Năm kết thúc phải lớn hơn hoặc bằng năm bắt đầu!");
      return;
    }

    if (modalMode === "create") {
      const newId = data.length > 0 ? Math.max(...data.map((d) => d.id)) + 1 : 1;
      const newCohort: AcademicYear = {
        id: newId,
        code: formData.code.trim(),
        description: formData.description.trim() || `Lớp sinh viên niên khóa ${formData.startYear} - ${formData.endYear}`,
        startYear: Number(formData.startYear),
        endYear: Number(formData.endYear),
        status: formData.status,
        isLocked: false,
      };
      setData([newCohort, ...data]);
      showToast(`Đã thêm mới niên khóa ${newCohort.code}`);
    } else if (modalMode === "edit" && currentCohort) {
      setData((prev) =>
        prev.map((item) =>
          item.id === currentCohort.id
            ? {
                ...item,
                code: formData.code.trim(),
                description: formData.description.trim(),
                startYear: Number(formData.startYear),
                endYear: Number(formData.endYear),
                status: formData.status,
              }
            : item
        )
      );
      showToast(`Đã cập nhật niên khóa ${formData.code.trim()}`);
    }
    setIsModalOpen(false);
  };

  // Export CSV
  const handleExportCSV = () => {
    const headers = ["TT,Niên khóa,Mô tả,Năm bắt đầu,Năm kết thúc,Trạng thái,Đã khóa"];
    const rows = filteredData.map((d, index) =>
      [
        index + 1,
        `"${d.code}"`,
        `"${d.description}"`,
        d.startYear,
        d.endYear,
        `"${d.status}"`,
        d.isLocked ? "Có" : "Không",
      ].join(",")
    );
    const csvContent = "\uFEFF" + [headers, ...rows].join("\n");
    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.setAttribute("href", url);
    link.setAttribute("download", `danh_sach_nien_khoa_${new Date().toISOString().slice(0, 10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showToast("Đã xuất danh sách niên khóa ra file CSV thành công!");
  };

  // Filtered and Sorted Data
  const filteredData = useMemo(() => {
    let result = [...data];

    // Filter by search term
    if (searchTerm.trim()) {
      const term = searchTerm.toLowerCase();
      result = result.filter(
        (item) =>
          item.code.toLowerCase().includes(term) ||
          item.description.toLowerCase().includes(term) ||
          item.startYear.toString().includes(term) ||
          item.endYear.toString().includes(term)
      );
    }

    // Filter by status
    if (statusFilter !== "Tất cả") {
      result = result.filter((item) => item.status === statusFilter);
    }

    // Sort
    if (sortField) {
      result.sort((a, b) => {
        let valA = a[sortField];
        let valB = b[sortField];

        if (typeof valA === "string" && typeof valB === "string") {
          return sortOrder === "asc"
            ? valA.localeCompare(valB)
            : valB.localeCompare(valA);
        }
        if (typeof valA === "number" && typeof valB === "number") {
          return sortOrder === "asc" ? valA - valB : valB - valA;
        }
        return 0;
      });
    }

    return result;
  }, [data, searchTerm, statusFilter, sortField, sortOrder]);

  const sidebarLinks = [
    { title: "Dashboard", icon: "dashboard" },
    { title: "Quản lý niên khóa", icon: "calendar", active: true },
    { title: "Quản lý khung chương trình", icon: "program", hasSub: true },
    { title: "Quản lý học kỳ", icon: "clock" },
    { title: "Quản lý nhóm môn học", icon: "group" },
    { title: "Quản lý môn học", icon: "book" },
    { title: "Quản lý lớp của môn học", icon: "class" },
    { title: "Quản lý phòng học", icon: "room" },
    { title: "Quản lý cấp bậc đào tạo", icon: "badge" },
    { title: "Quản lý khoa", icon: "building" },
    { title: "Quản lý chuyên ngành", icon: "branch" },
    { title: "Quản lý nhóm ngành", icon: "category" },
    { title: "Quản lý giảng viên", icon: "teacher" },
    { title: "Quản lý sinh viên", icon: "student", hasSub: true },
    { title: "Nhận xét & Phản hồi", icon: "chat" },
    { title: "Quản lý Chuẩn đầu ra (CĐR)", icon: "shield", hasSub: true },
  ];

  return (
    <div className="h-full w-full font-sans text-slate-700 dark:text-slate-200 antialiased overflow-hidden flex bg-slate-50 dark:bg-slate-900 transition-colors duration-200">
      {/* Toast Notification */}
      {toastMessage && (
        <div className="fixed bottom-5 right-5 z-50 bg-slate-900 dark:bg-slate-100 text-white dark:text-slate-900 text-xs px-4 py-2.5 shadow-lg border border-slate-700 dark:border-slate-300 flex items-center gap-2 animate-bounce">
          <svg className="w-4 h-4 text-brand-500 dark:text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path d="M5 13l4 4L19 7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
          </svg>
          <span>{toastMessage}</span>
        </div>
      )}

      {/* BEGIN: Sidebar */}
      <aside className="w-72 bg-white dark:bg-slate-950 border-r border-slate-200 dark:border-slate-800 flex flex-col flex-shrink-0 select-none z-20 transition-colors duration-200">
        {/* Brand Header */}
        <div className="h-16 px-6 border-b border-slate-100 dark:border-slate-800 flex items-center gap-3">
          <div className="w-10 h-10 flex-shrink-0 flex items-center justify-center overflow-hidden">
            <img
              alt="Vietnam Japan University Logo"
              className="w-full h-full object-contain"
              src="/vju-logo.png"
            />
          </div>
          <div className="flex flex-col">
            <span className="text-xs font-bold tracking-tight text-slate-900 dark:text-slate-100 leading-tight">
              QUẢN LÝ MÔN HỌC
            </span>
            <span className="text-[10px] text-slate-500 dark:text-slate-400 font-medium leading-tight tracking-wider uppercase">
              ĐÁNH GIÁ CHUẨN ĐẦU RA
            </span>
          </div>
        </div>

        {/* Navigation Links */}
        <nav className="flex-1 overflow-y-auto px-3 py-4 space-y-1 custom-scrollbar text-xs font-medium">
          {sidebarLinks.map((link) => {
            const isActive = activeMenu === link.title;
            return (
              <a
                key={link.title}
                onClick={(e) => {
                  e.preventDefault();
                  setActiveMenu(link.title);
                }}
                href="#"
                className={`flex items-center justify-between px-3 py-2 rounded-none transition-colors ${
                  isActive
                    ? "text-brand-600 dark:text-brand-400 bg-brand-50/80 dark:bg-brand-950/60 font-semibold border border-brand-100 dark:border-brand-900/50"
                    : "text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-100 hover:bg-slate-100/70 dark:hover:bg-slate-800/60"
                }`}
              >
                <div className="flex items-center gap-3">
                  <svg className={`w-4 h-4 ${isActive ? "text-brand-600 dark:text-brand-400" : "text-slate-400 dark:text-slate-500"}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    {link.icon === "dashboard" && <path d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "calendar" && <path d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "program" && <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "clock" && <path d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "group" && <path d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "book" && <path d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "class" && <path d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "room" && <path d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "badge" && <path d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "building" && <path d="M8 14v3m4-3v3m4-3v3M3 21h18M3 10h18M3 7l9-4 9 4M4 10h16v11H4V10z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "branch" && <path d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "category" && <path d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "teacher" && <path d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "student" && <path d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "chat" && <path d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                    {link.icon === "shield" && <path d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />}
                  </svg>
                  <span>{link.title}</span>
                </div>
                {isActive && <span className="w-1.5 h-1.5 rounded-none bg-brand-600 dark:bg-brand-400"></span>}
                {link.hasSub && !isActive && (
                  <svg className="w-3.5 h-3.5 text-slate-400 dark:text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path d="M19 9l-7 7-7-7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                  </svg>
                )}
              </a>
            );
          })}
        </nav>
      </aside>
      {/* END: Sidebar */}

      {/* BEGIN: Main Workspace Area */}
      <div className="flex-1 flex flex-col min-w-0 overflow-hidden bg-slate-50 dark:bg-slate-900 transition-colors duration-200">
        {/* BEGIN: Top Header Bar */}
        <header className="h-16 bg-white dark:bg-slate-950 border-b border-slate-200 dark:border-slate-800 px-8 flex items-center justify-between flex-shrink-0 transition-colors duration-200">
          {/* Breadcrumbs */}
          <nav className="flex items-center text-xs text-slate-500 dark:text-slate-400 font-medium gap-2">
            <a className="hover:text-slate-800 dark:hover:text-slate-200 transition-colors" href="#">Trang chủ</a>
            <svg className="w-3.5 h-3.5 text-slate-300 dark:text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path d="M9 5l7 7-7 7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
            </svg>
            <span className="text-slate-800 dark:text-slate-100 font-semibold">Quản lý niên khóa</span>
          </nav>

          {/* Right Header Controls */}
          <div className="flex items-center gap-4">
            {/* Year Filter with Quick Switchers */}
            <div className="inline-flex items-center bg-slate-50/80 dark:bg-slate-900/80 border border-slate-200 dark:border-slate-800 rounded-none p-1 shadow-xs hover:border-slate-300 dark:hover:border-slate-700 transition-colors h-9">
              <div className="flex items-center gap-1.5 px-2 py-0.5 border-r border-slate-200 dark:border-slate-800 text-slate-500 dark:text-slate-400">
                <svg className="w-3.5 h-3.5 text-brand-600 dark:text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                </svg>
              </div>
              <button
                onClick={handlePrevYear}
                disabled={selectedSchoolYearIndex === 0}
                className="w-6 h-6 flex items-center justify-center text-slate-400 hover:text-brand-600 dark:hover:text-brand-400 hover:bg-white dark:hover:bg-slate-800 rounded-none transition-colors disabled:opacity-30 cursor-pointer"
                title="Năm học trước"
                type="button"
              >
                <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path d="M15 19l-7-7 7-7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                </svg>
              </button>
              <div className="relative">
                <select
                  value={SCHOOL_YEARS[selectedSchoolYearIndex]}
                  onChange={(e) => setSelectedSchoolYearIndex(SCHOOL_YEARS.indexOf(e.target.value))}
                  className="appearance-none bg-transparent hover:bg-white dark:hover:bg-slate-800 text-xs font-semibold text-slate-800 dark:text-slate-200 focus:outline-none cursor-pointer rounded-none border border-transparent hover:border-slate-200 dark:hover:border-slate-700 transition-colors pl-3 pr-8 py-1.5"
                >
                  {SCHOOL_YEARS.map((y) => (
                    <option key={y} className="dark:bg-slate-900 text-slate-800 dark:text-slate-200" value={y}>
                      {y}
                    </option>
                  ))}
                </select>
              </div>
              <button
                onClick={handleNextYear}
                disabled={selectedSchoolYearIndex === SCHOOL_YEARS.length - 1}
                className="w-6 h-6 flex items-center justify-center text-slate-400 hover:text-brand-600 dark:hover:text-brand-400 hover:bg-white dark:hover:bg-slate-800 rounded-none transition-colors disabled:opacity-30 cursor-pointer"
                title="Năm học kế tiếp"
                type="button"
              >
                <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path d="M9 5l7 7-7 7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                </svg>
              </button>
            </div>

            {/* Notification Bell */}
            <button
              onClick={() => showToast("Không có thông báo mới")}
              className="relative flex items-center justify-center bg-slate-50/80 dark:bg-slate-900/80 border border-slate-200 dark:border-slate-800 rounded-none text-slate-500 dark:text-slate-300 hover:text-brand-600 dark:hover:text-brand-400 hover:border-slate-300 dark:hover:border-slate-700 hover:bg-white dark:hover:bg-slate-800 shadow-xs transition-colors h-9 w-9 cursor-pointer"
              title="Thông báo"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
              </svg>
              <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-none bg-brand-600 dark:bg-brand-400 ring-2 ring-white dark:ring-slate-950"></span>
            </button>

            {/* Dark / Light Mode Toggle Button */}
            <button
              aria-label="Toggle theme"
              onClick={toggleTheme}
              className="flex items-center justify-center bg-slate-50/80 dark:bg-slate-900/80 border border-slate-200 dark:border-slate-800 rounded-none text-slate-500 dark:text-slate-300 hover:text-slate-700 dark:hover:text-slate-100 hover:border-slate-300 dark:hover:border-slate-700 hover:bg-white dark:hover:bg-slate-800 shadow-xs transition-colors h-9 w-9 cursor-pointer"
              title={isDarkMode ? "Chuyển sang Chế độ Sáng" : "Chuyển sang Chế độ Tối"}
              type="button"
            >
              {isDarkMode ? (
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                </svg>
              ) : (
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                </svg>
              )}
            </button>

            <div className="h-4 w-px bg-slate-200 dark:bg-slate-800"></div>

            {/* Admin Profile */}
            <div className="flex items-center gap-3 pl-1">
              <button
                className="w-9 h-9 rounded-none bg-brand-100 dark:bg-brand-950 text-brand-700 dark:text-brand-300 flex items-center justify-center font-semibold text-xs border border-brand-200 dark:border-brand-800 ring-2 ring-white dark:ring-slate-900 hover:ring-brand-200 dark:hover:ring-brand-700 transition-all cursor-pointer shadow-xs"
                title="Tài khoản: Admin Quản Trị"
                type="button"
              >
                AD
              </button>
            </div>
          </div>
        </header>
        {/* END: Top Header Bar */}

        {/* BEGIN: Main Content Area */}
        <main className="flex-1 overflow-y-auto custom-scrollbar bg-slate-50 dark:bg-slate-900 transition-colors duration-200 flex flex-col">
          {/* Page Title & Primary Actions */}
          <section className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white dark:bg-slate-900 pt-3 px-6 pb-3 border-b border-slate-200 dark:border-slate-800">
            {/* Search Input */}
            <div className="flex items-center gap-2 flex-1 max-w-sm">
              <div className="relative w-full">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-400 dark:text-slate-500">
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                  </svg>
                </div>
                <input
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="block w-full pl-9 pr-8 py-1.5 text-xs text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 border border-slate-200 dark:border-slate-800 rounded-none focus:ring-1 focus:ring-brand-500 focus:border-brand-500 bg-white dark:bg-slate-950 hover:border-slate-300 dark:hover:border-slate-700 transition-colors shadow-xs"
                  placeholder="Tìm kiếm niên khóa..."
                  type="text"
                />
                {searchTerm && (
                  <button
                    onClick={() => setSearchTerm("")}
                    className="absolute inset-y-0 right-0 pr-2.5 flex items-center text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 cursor-pointer"
                    title="Xóa tìm kiếm"
                  >
                    ×
                  </button>
                )}
              </div>
            </div>

            {/* Action Buttons */}
            <div className="flex items-center gap-2">
              {/* Export Excel Button */}
              <button
                onClick={handleExportCSV}
                className="w-8 h-8 flex items-center justify-center rounded-none border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 text-slate-600 dark:text-slate-300 hover:text-slate-900 dark:hover:text-slate-100 hover:bg-slate-50 dark:hover:bg-slate-800 hover:border-slate-300 dark:hover:border-slate-700 transition-all shadow-xs active:translate-y-px cursor-pointer"
                title="Xuất file Excel/CSV"
                type="button"
              >
                <svg className="w-4 h-4 text-brand-600 dark:text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                </svg>
              </button>

              {/* Add Cohort Button */}
              <button
                onClick={handleOpenCreate}
                className="h-8 flex items-center rounded-none border border-brand-600 bg-brand-600 hover:bg-brand-700 text-white transition-all shadow-xs text-xs font-medium active:translate-y-px w-8 justify-center cursor-pointer"
                title="Thêm mới niên khóa"
                type="button"
              >
                <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path d="M12 4v16m8-8H4" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                </svg>
              </button>
            </div>
          </section>

          {/* Data Table Card */}
          <section className="bg-white dark:bg-slate-950 border border-slate-200/90 dark:border-slate-800 rounded-none shadow-sm overflow-hidden flex flex-col transition-colors duration-200 flex-1">
            <div className="overflow-x-auto custom-scrollbar flex-1">
              <table className="w-full table-fixed text-left border-collapse text-xs">
                <colgroup>
                  <col className="w-[60px]" />
                  <col className="w-[140px]" />
                  <col className="" />
                  <col className="w-[120px]" />
                  <col className="w-[120px]" />
                  <col className="w-[160px]" />
                  <col className="w-[140px]" />
                </colgroup>
                <thead>
                  <tr className="bg-slate-50 dark:bg-slate-900/60 border-b border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-400 font-semibold uppercase tracking-wider text-[11px]">
                    <th className="py-3 px-4 text-center" scope="col">TT</th>
                    <th className="py-3 px-4" scope="col">
                      <div
                        onClick={() => handleSort("code")}
                        className="flex items-center justify-between gap-1 group cursor-pointer hover:text-brand-600 dark:hover:text-brand-400 transition-colors"
                      >
                        <span className="truncate">Niên khóa</span>
                        <button className="p-1 text-slate-400 hover:text-brand-600 dark:hover:text-brand-400 rounded-none transition-colors flex-shrink-0" title="Sắp xếp tên" type="button">
                          <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                          </svg>
                        </button>
                      </div>
                    </th>
                    <th className="py-3 px-4" scope="col">
                      <div className="flex items-center justify-between gap-1 group cursor-pointer hover:text-brand-600 dark:hover:text-brand-400 transition-colors">
                        <span className="truncate">Mô tả</span>
                      </div>
                    </th>
                    <th className="py-3 px-4 text-center" scope="col">
                      <div
                        onClick={() => handleSort("startYear")}
                        className="flex items-center justify-center gap-1 group cursor-pointer hover:text-brand-600 dark:hover:text-brand-400 transition-colors"
                      >
                        <span className="truncate">Năm bắt đầu</span>
                        <button className="p-1 text-slate-400 hover:text-brand-600 dark:hover:text-brand-400 rounded-none transition-colors flex-shrink-0" title="Sắp xếp năm bắt đầu" type="button">
                          <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                          </svg>
                        </button>
                      </div>
                    </th>
                    <th className="py-3 px-4 text-center" scope="col">
                      <div
                        onClick={() => handleSort("endYear")}
                        className="flex items-center justify-center gap-1 group cursor-pointer hover:text-brand-600 dark:hover:text-brand-400 transition-colors"
                      >
                        <span className="truncate">Năm kết thúc</span>
                        <button className="p-1 text-slate-400 hover:text-brand-600 dark:hover:text-brand-400 rounded-none transition-colors flex-shrink-0" title="Sắp xếp năm kết thúc" type="button">
                          <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                          </svg>
                        </button>
                      </div>
                    </th>
                    <th className="py-3 px-4 text-center relative" scope="col">
                      <div
                        onClick={() => setShowStatusFilterMenu(!showStatusFilterMenu)}
                        className="inline-flex items-center justify-center gap-1.5 px-2 py-0.5 rounded-none hover:bg-slate-200/60 dark:hover:bg-slate-800 cursor-pointer transition-colors"
                        title="Lọc theo trạng thái"
                      >
                        <span className="truncate">Trạng thái</span>
                        <svg className="w-3.5 h-3.5 text-brand-600 dark:text-brand-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                        </svg>
                      </div>
                      {/* Status Filter Dropdown */}
                      {showStatusFilterMenu && (
                        <div className="absolute right-4 top-10 w-44 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 shadow-lg z-30 py-1 text-left">
                          {["Tất cả", "Sắp tuyển sinh", "Chuẩn bị nhập học", "Đang đào tạo", "Đã tốt nghiệp", "Chưa mở"].map((st) => (
                            <button
                              key={st}
                              onClick={() => {
                                setStatusFilter(st);
                                setShowStatusFilterMenu(false);
                              }}
                              className={`w-full text-left px-3 py-1.5 text-xs hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors flex items-center justify-between ${
                                statusFilter === st ? "text-brand-600 font-semibold" : "text-slate-700 dark:text-slate-300"
                              }`}
                            >
                              <span>{st}</span>
                              {statusFilter === st && <span>✓</span>}
                            </button>
                          ))}
                        </div>
                      )}
                    </th>
                    <th className="py-3 px-4 text-center" scope="col">Thao tác</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 dark:divide-slate-850 font-normal">
                  {filteredData.length === 0 ? (
                    <tr>
                      <td colSpan={7} className="py-8 text-center text-slate-400">
                        Không tìm thấy niên khóa nào phù hợp.
                      </td>
                    </tr>
                  ) : (
                    filteredData.map((item, index) => (
                      <tr key={item.id} className="hover:bg-slate-50/80 dark:hover:bg-slate-900/50 transition-colors group">
                        <td className="py-2.5 px-4 text-center font-medium text-slate-400 dark:text-slate-500">
                          {index + 1}
                        </td>
                        <td className="py-2.5 px-4 truncate">
                          <button
                            onClick={() => handleOpenView(item)}
                            className="hover:underline text-slate-900 dark:text-slate-100 hover:text-brand-600 dark:hover:text-brand-400 font-medium text-left cursor-pointer"
                          >
                            {item.code}
                          </button>
                        </td>
                        <td className="py-2.5 px-4 text-slate-600 dark:text-slate-300 truncate">
                          {item.description}
                        </td>
                        <td className="py-2.5 px-4 text-center text-slate-600 dark:text-slate-300 font-medium">
                          {item.startYear}
                        </td>
                        <td className="py-2.5 px-4 text-center text-slate-600 dark:text-slate-300 font-medium">
                          {item.endYear}
                        </td>
                        <td className="py-2.5 px-4 text-center">
                          <span className="inline-flex items-center justify-center text-center w-[130px] py-1 rounded-none text-[11px] font-medium bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-700 whitespace-nowrap shadow-xs">
                            {item.status}
                          </span>
                        </td>
                        <td className="py-2.5 px-4 text-center">
                          <div className="flex items-center justify-center gap-1.5 opacity-90 group-hover:opacity-100">
                            {/* View Button */}
                            <button
                              onClick={() => handleOpenView(item)}
                              className="w-7 h-7 flex items-center justify-center text-slate-400 hover:text-brand-600 dark:hover:text-brand-400 hover:bg-brand-50 dark:hover:bg-brand-950/50 rounded-none border border-transparent hover:border-brand-200 dark:hover:border-brand-900/50 transition-colors cursor-pointer"
                              title="Xem chi tiết"
                              type="button"
                            >
                              <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                                <path d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                              </svg>
                            </button>

                            {/* Edit Button */}
                            <button
                              onClick={() => handleOpenEdit(item)}
                              className="w-7 h-7 flex items-center justify-center text-slate-400 hover:text-brand-600 dark:hover:text-brand-400 hover:bg-brand-50 dark:hover:bg-brand-950/50 rounded-none border border-transparent hover:border-brand-200 dark:hover:border-brand-900/50 transition-colors cursor-pointer"
                              title="Chỉnh sửa"
                              type="button"
                            >
                              <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                              </svg>
                            </button>

                            {/* Lock Button */}
                            <button
                              onClick={() => handleToggleLock(item.id)}
                              className="w-7 h-7 flex items-center justify-center text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-none border border-transparent hover:border-slate-200 dark:hover:border-slate-700 transition-colors cursor-pointer"
                              title={item.isLocked ? "Mở khóa niên khóa" : "Khóa niên khóa"}
                              type="button"
                            >
                              <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                              </svg>
                            </button>

                            {/* Delete Button */}
                            <button
                              onClick={() => setDeleteConfirmId(item.id)}
                              className="w-7 h-7 flex items-center justify-center text-slate-400 hover:text-rose-600 dark:hover:text-rose-400 hover:bg-rose-50 dark:hover:bg-rose-950/50 rounded-none border border-transparent hover:border-rose-200 dark:hover:border-rose-900/50 transition-colors cursor-pointer"
                              title="Xóa"
                              type="button"
                            >
                              <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                              </svg>
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {/* Pagination Footer */}
            <div className="px-6 py-3 border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 flex flex-col sm:flex-row items-center justify-between gap-3 text-xs flex-shrink-0">
              <div className="flex items-center gap-2 text-slate-500 dark:text-slate-400">
                <span className="text-[11px]">
                  Hiển thị <span className="font-medium text-slate-700 dark:text-slate-200">{filteredData.length > 0 ? 1 : 0} - {filteredData.length}</span> trong số{" "}
                  <span className="font-medium text-slate-700 dark:text-slate-200">{filteredData.length}</span> niên khóa
                </span>
              </div>
              <div className="flex items-center gap-2">
                <div className="inline-flex items-center space-x-1 border border-slate-200 dark:border-slate-800 rounded-none p-0.5 bg-slate-50/80 dark:bg-slate-900">
                  <button className="w-7 h-7 flex items-center justify-center rounded-none text-slate-400 dark:text-slate-500 disabled:opacity-40" disabled title="Trang trước">
                    <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path d="M15 19l-7-7 7-7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                    </svg>
                  </button>
                  <button className="w-7 h-7 text-xs font-semibold rounded-none bg-brand-600 text-white shadow-xs flex items-center justify-center">
                    1
                  </button>
                  <button className="w-7 h-7 text-xs font-medium rounded-none text-slate-600 dark:text-slate-400 hover:bg-white dark:hover:bg-slate-800 hover:text-slate-900 dark:hover:text-slate-100 transition-colors flex items-center justify-center">
                    2
                  </button>
                  <button className="w-7 h-7 text-xs font-medium rounded-none text-slate-600 dark:text-slate-400 hover:bg-white dark:hover:bg-slate-800 hover:text-slate-900 dark:hover:text-slate-100 transition-colors flex items-center justify-center">
                    3
                  </button>
                  <span className="px-1 text-slate-400 text-xs font-light">...</span>
                  <button className="w-7 h-7 text-xs font-medium rounded-none text-slate-600 dark:text-slate-400 hover:bg-white dark:hover:bg-slate-800 hover:text-slate-900 dark:hover:text-slate-100 transition-colors flex items-center justify-center">
                    7
                  </button>
                  <button className="w-7 h-7 flex items-center justify-center rounded-none text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-100 hover:bg-white dark:hover:bg-slate-800 transition-colors cursor-pointer" title="Trang kế tiếp">
                    <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path d="M9 5l7 7-7 7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </section>
        </main>
        {/* END: Main Content Area */}
      </div>
      {/* END: Main Workspace Area */}

      {/* Modal Thêm Mới / Chỉnh Sửa / Xem Chi Tiết */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 bg-slate-950/50 backdrop-blur-xs flex items-center justify-center p-4">
          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 w-full max-w-md shadow-2xl p-6">
            <div className="flex items-center justify-between border-b border-slate-100 dark:border-slate-800 pb-3 mb-4">
              <h3 className="text-sm font-bold text-slate-900 dark:text-slate-100">
                {modalMode === "create" && "Thêm mới niên khóa"}
                {modalMode === "edit" && "Chỉnh sửa niên khóa"}
                {modalMode === "view" && "Chi tiết niên khóa"}
              </h3>
              <button
                onClick={() => setIsModalOpen(false)}
                className="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 text-lg leading-none cursor-pointer"
              >
                ×
              </button>
            </div>

            {modalMode === "view" && currentCohort ? (
              <div className="space-y-3 text-xs">
                <div>
                  <span className="font-semibold text-slate-500 dark:text-slate-400">Niên khóa:</span>
                  <p className="text-slate-900 dark:text-slate-100 font-bold text-sm mt-0.5">{currentCohort.code}</p>
                </div>
                <div>
                  <span className="font-semibold text-slate-500 dark:text-slate-400">Mô tả:</span>
                  <p className="text-slate-700 dark:text-slate-300 mt-0.5">{currentCohort.description}</p>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <span className="font-semibold text-slate-500 dark:text-slate-400">Năm bắt đầu:</span>
                    <p className="text-slate-900 dark:text-slate-100 font-medium mt-0.5">{currentCohort.startYear}</p>
                  </div>
                  <div>
                    <span className="font-semibold text-slate-500 dark:text-slate-400">Năm kết thúc:</span>
                    <p className="text-slate-900 dark:text-slate-100 font-medium mt-0.5">{currentCohort.endYear}</p>
                  </div>
                </div>
                <div>
                  <span className="font-semibold text-slate-500 dark:text-slate-400">Trạng thái:</span>
                  <p className="mt-1">
                    <span className="inline-block px-2.5 py-1 bg-slate-100 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 font-medium text-[11px]">
                      {currentCohort.status}
                    </span>
                  </p>
                </div>
                <div>
                  <span className="font-semibold text-slate-500 dark:text-slate-400">Tình trạng khóa:</span>
                  <p className="text-slate-700 dark:text-slate-300 mt-0.5">
                    {currentCohort.isLocked ? "🔒 Đang bị khóa" : "🔓 Đang mở"}
                  </p>
                </div>
                <div className="pt-4 border-t border-slate-100 dark:border-slate-800 flex justify-end">
                  <button
                    onClick={() => setIsModalOpen(false)}
                    className="px-4 py-1.5 bg-slate-100 hover:bg-slate-200 dark:bg-slate-800 dark:hover:bg-slate-700 text-slate-800 dark:text-slate-200 text-xs font-medium cursor-pointer"
                  >
                    Đóng
                  </button>
                </div>
              </div>
            ) : (
              <form onSubmit={handleFormSubmit} className="space-y-4 text-xs">
                <div>
                  <label className="block font-medium text-slate-700 dark:text-slate-300 mb-1">
                    Niên khóa (Mã) *
                  </label>
                  <input
                    type="text"
                    required
                    value={formData.code}
                    onChange={(e) => setFormData({ ...formData, code: e.target.value })}
                    placeholder="Ví dụ: K2028"
                    className="w-full px-3 py-1.5 border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 text-slate-900 dark:text-slate-100 focus:outline-none focus:border-brand-500"
                  />
                </div>

                <div>
                  <label className="block font-medium text-slate-700 dark:text-slate-300 mb-1">
                    Mô tả
                  </label>
                  <input
                    type="text"
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    placeholder="Ví dụ: Lớp sinh viên niên khóa 2028 - 2032"
                    className="w-full px-3 py-1.5 border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 text-slate-900 dark:text-slate-100 focus:outline-none focus:border-brand-500"
                  />
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="block font-medium text-slate-700 dark:text-slate-300 mb-1">
                      Năm bắt đầu *
                    </label>
                    <input
                      type="number"
                      required
                      value={formData.startYear}
                      onChange={(e) => setFormData({ ...formData, startYear: Number(e.target.value) })}
                      className="w-full px-3 py-1.5 border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 text-slate-900 dark:text-slate-100 focus:outline-none focus:border-brand-500"
                    />
                  </div>
                  <div>
                    <label className="block font-medium text-slate-700 dark:text-slate-300 mb-1">
                      Năm kết thúc *
                    </label>
                    <input
                      type="number"
                      required
                      value={formData.endYear}
                      onChange={(e) => setFormData({ ...formData, endYear: Number(e.target.value) })}
                      className="w-full px-3 py-1.5 border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 text-slate-900 dark:text-slate-100 focus:outline-none focus:border-brand-500"
                    />
                  </div>
                </div>

                <div>
                  <label className="block font-medium text-slate-700 dark:text-slate-300 mb-1">
                    Trạng thái
                  </label>
                  <select
                    value={formData.status}
                    onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                    className="w-full px-3 py-1.5 border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 text-slate-900 dark:text-slate-100 focus:outline-none focus:border-brand-500"
                  >
                    <option value="Sắp tuyển sinh">Sắp tuyển sinh</option>
                    <option value="Chuẩn bị nhập học">Chuẩn bị nhập học</option>
                    <option value="Đang đào tạo">Đang đào tạo</option>
                    <option value="Đã tốt nghiệp">Đã tốt nghiệp</option>
                    <option value="Chưa mở">Chưa mở</option>
                  </select>
                </div>

                <div className="pt-4 border-t border-slate-100 dark:border-slate-800 flex justify-end gap-2">
                  <button
                    type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="px-4 py-1.5 bg-slate-100 hover:bg-slate-200 dark:bg-slate-800 dark:hover:bg-slate-700 text-slate-800 dark:text-slate-200 text-xs font-medium cursor-pointer"
                  >
                    Hủy
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-1.5 bg-brand-600 hover:bg-brand-700 text-white text-xs font-medium cursor-pointer"
                  >
                    {modalMode === "create" ? "Thêm mới" : "Lưu thay đổi"}
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      )}

      {/* Delete Confirmation Modal */}
      {deleteConfirmId !== null && (
        <div className="fixed inset-0 z-50 bg-slate-950/50 backdrop-blur-xs flex items-center justify-center p-4">
          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 w-full max-w-sm shadow-2xl p-5">
            <h4 className="font-bold text-sm text-slate-900 dark:text-slate-100 mb-2">
              Xác nhận xóa niên khóa
            </h4>
            <p className="text-xs text-slate-600 dark:text-slate-400 mb-4">
              Bạn có chắc chắn muốn xóa niên khóa{" "}
              <strong className="text-slate-900 dark:text-slate-200">
                {data.find((d) => d.id === deleteConfirmId)?.code}
              </strong>
              ? Hành động này không thể hoàn tác.
            </p>
            <div className="flex justify-end gap-2 text-xs">
              <button
                onClick={() => setDeleteConfirmId(null)}
                className="px-3 py-1.5 bg-slate-100 hover:bg-slate-200 dark:bg-slate-800 dark:hover:bg-slate-700 text-slate-800 dark:text-slate-200 font-medium cursor-pointer"
              >
                Hủy
              </button>
              <button
                onClick={() => handleDelete(deleteConfirmId)}
                className="px-3 py-1.5 bg-rose-600 hover:bg-rose-700 text-white font-medium cursor-pointer"
              >
                Xác nhận xóa
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}