module Main where

  main :: IO()
  main = do
    putStrLn "Enter frontnesses (F2):"
    frontsM<-getLine
    let fronts = map (read::String->Double) $ words frontsM
    putStrLn "Enter closenesses (F1):"
    closesM<-getLine
    let closes = map (read::String->Double) $ words closesM
    mapM_ print (entojp (zip fronts closes))
    _<-getLine
    putStrLn "Closing..."

  front :: (a,b)->a
  front=fst
  close :: (a,b)->b
  close=snd

  avg :: Fractional a =>(t->a) -> t -> t -> a
  avg f x y = (f x+f y)/2

  entojp :: Fractional a => [(a, a)]-> [(a, a)]
  entojp [a, i, u', e, o] = let ɨ = (avg front i u', close u') --weird unicode characters for the win
                                ɛ = (avg front e a, avg close e a)
                                æ = (avg front ɛ a, avg close ɛ a)
                                ɪ = (avg front i e, close ɛ)
                                ʊ = (avg front ɨ u', avg close u' o)
                                ʌ = (front u', close ɛ)
                                ɑ = (front u', close a)
                                ɜ = (avg front ɛ ʌ, close ɛ)
                                mid1 = (avg front a ɑ, close a)
                                ɐ = (avg front ɜ mid1, close æ)
                                mid2 = (avg front e o, close e)
                                ə = (avg front mid2 ɜ, avg close mid2 ɜ)
                            in [ɨ, ɛ, æ, ɪ, ʊ, ʌ, ɑ, ɜ, ɐ, ə]
