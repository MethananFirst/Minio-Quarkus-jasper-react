import React, { useEffect, useState } from "react";
import AddBucket from "../component/AddBucket.jsx";
import { Link } from "react-router-dom";
import Layout from "./Layout.jsx";

const Bucket = () => {
    const [buckets, setBuckets] = useState([]);

    useEffect(() => {
        const getBuckets = async () => {
            try {
                const res = await fetch("http://localhost:8080/minio/listAllBuckets", {
                    method: "GET",
                });
                const data = await res.json();
                setBuckets(data);
            } catch (error) {
                console.error("Error fetching buckets:", error.message);
            }
        };

        getBuckets();
    }, []);

    const deleteBucket = async (bucket) => {
        try {
            const res = await fetch(`http://localhost:8080/minio/deleteBucket/${bucket}`, {
                method: "DELETE",
            });

            if (res.ok) {
                alert("Successfully deleted!");
                setBuckets((prevBuckets) => prevBuckets.filter((b) => b !== bucket));
            } else {
                alert("Failed to delete");
            }
        } catch (error) {
            console.error("Error deleting bucket:", error.message);
            alert("Failed to delete");
        }
    };

    return (
    <>
        <Layout/>
        <div className="mx-auto w-[500px] border-2 border-fuchsia-700 p-12 my-auto bg-blue-400">
            <div className="w-full my-5 justify-end flex">
                <AddBucket/>
            </div>
            <div className="border-2 bg-blue-300">
                <div>
                    <h1 className="font-bold text-center">Bucket List</h1>
                </div>
                <div className="grid grid-cols-2 justify-between p-10 gap-3">
                    {buckets.map((bucket) => (
                        <div key={bucket} className="flex items-center justify-between">
                            <Link to={`allFile/${bucket}`}>
                                <p className="text-justify">{bucket}</p>
                            </Link>
                            <button onClick={() => deleteBucket(bucket)} className="border-2 border-black bg-blue-600">
                                Delete
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    </>
)
    ;
};

export default Bucket;
