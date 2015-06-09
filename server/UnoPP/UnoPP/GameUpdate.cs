using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class GameUpdate
    {
        public Result Result;
        public List<Update> Updates;

        public GameUpdate()
        {

        }

        public GameUpdate(Result result, List<Update> updates)
        {
            this.Result = result;
            this.Updates = new List<Update>();

            if (updates != null)
            {
                for (int i = 0; i < updates.Count(); i++)
                {
                    this.Updates.Add(updates[i]);
                }
            }
        }
    }
}