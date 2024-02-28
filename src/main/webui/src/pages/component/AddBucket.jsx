import {useState} from "react";

function AddBucket(){
    const [nameBucket, setNameBucket] = useState(null)

    const inputBucket = async () => {
        try{
            if (nameBucket == null) {
                alert("Please add name Bucket")
                return
            }

            const res = await  fetch(`http://localhost:8080/minio/createBucket/${nameBucket}`, {
                method: "POST"
            })
            if (res.ok) {
                alert("OK")
            }
        }
        catch (error) {
            alert("Bucket Adding Error!! : ", error)
        }
    }


    return (
        <div>
            <input type="text" onChange={(e) => setNameBucket(e.target.value)} className="border"/>
            <button onClick={inputBucket} className="border-2 border-black bg-blue-500">Create Bucket</button>
        </div>
    )
}

export default AddBucket;