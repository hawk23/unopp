using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UnoTest
{
    class Program
    {
        static void Main(string[] args)
        {
            uno.UnoPP p = new uno.UnoPP();

            uno.Player player1 = p.CreatePlayer("Julius", 0L, 0L);
            uno.Player player2 = p.CreatePlayer("Peter", 0L, 0L);


        }
    }
}
