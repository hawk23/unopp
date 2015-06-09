using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class Update
    {
        public int updateID;
        public string update;

        public Update()
        {

        }

        public Update(int id, string update)
        {
            this.updateID = id;
            this.update = update;
        }
    }
}