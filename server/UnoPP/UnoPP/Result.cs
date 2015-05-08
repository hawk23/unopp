using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class Result
    {
        public bool status;
        public string message;

        public Result()
        {

        }

        public Result(bool status, string message)
        {
            this.status = status;
            this.message = message;
        }
    }
}