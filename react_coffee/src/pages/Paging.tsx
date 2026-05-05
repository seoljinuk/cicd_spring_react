import { Pagination } from "react-bootstrap";
import type { PagingInfo } from "../types/Paging";

type Props = {
    paging: PagingInfo;
    setPaging: React.Dispatch<React.SetStateAction<PagingInfo>>;
};

function Paging({ paging, setPaging }: Props) {

    return (
        <Pagination className="justify-content-center mt-4">

            <Pagination.First
                onClick={() => setPaging(prev => ({ ...prev, pageNumber: 0 }))}
                disabled={paging.pageNumber < paging.pageCount}
            >
                맨처음
            </Pagination.First>

            <Pagination.Prev
                onClick={() => {
                    const gotoPage = paging.beginPage - 1;
                    setPaging(prev => ({ ...prev, pageNumber: gotoPage }));
                }}
                disabled={paging.pageNumber < paging.pageCount}
            >
                이전
            </Pagination.Prev>

            {[...Array(paging.endPage - paging.beginPage + 1)].map((_, idx) => {
                const pageIndex = paging.beginPage + idx + 1;

                return (
                    <Pagination.Item
                        key={pageIndex}
                        active={paging.pageNumber === (pageIndex - 1)}
                        onClick={() =>
                            setPaging(prev => ({
                                ...prev,
                                pageNumber: pageIndex - 1
                            }))
                        }
                    >
                        {pageIndex}
                    </Pagination.Item>
                );
            })}

            <Pagination.Next
                onClick={() => {
                    const gotoPage = paging.endPage + 1;
                    setPaging(prev => ({ ...prev, pageNumber: gotoPage }));
                }}
                disabled={
                    paging.pageNumber >=
                    Math.floor(paging.totalPages / paging.pageCount) * paging.pageCount
                }
            >
                다음
            </Pagination.Next>

            <Pagination.Last
                onClick={() => {
                    const gotoPage = paging.totalPages - 1;
                    setPaging(prev => ({ ...prev, pageNumber: gotoPage }));
                }}
                disabled={
                    paging.pageNumber >=
                    Math.floor(paging.totalPages / paging.pageCount) * paging.pageCount
                }
            >
                맨끝
            </Pagination.Last>

        </Pagination>
    );
}

export default Paging;