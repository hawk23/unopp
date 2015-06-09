using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class Card
    {
        private static int nextID = 0;

        public int id;
        
        public Card()
        {
            this.id = nextID++;
        }
    }
}