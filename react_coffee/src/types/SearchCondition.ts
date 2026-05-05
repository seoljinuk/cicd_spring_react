// 검색 조건 (SearchDto 대응)
export type SearchCondition = {
    searchDateType: string; // 기간 검색
    category: string;       // 카테고리
    searchMode: string;     // name | description
    searchKeyword: string;  // 키워드
};


export const initialSearchCondition: SearchCondition = {
    searchDateType: 'all',
    category: '',
    searchMode: '',
    searchKeyword: ''
};